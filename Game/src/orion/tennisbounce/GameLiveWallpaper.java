package orion.tennisbounce;

import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.ui.livewallpaper.BaseLiveWallpaperService;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.WindowManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class GameLiveWallpaper extends BaseLiveWallpaperService implements
        IAccelerationListener, IOnAreaTouchListener {

    private Camera mCamera;
    private Scene mScene;
    private TextureRegion mBallTexture;
    private float mCameraWidth;
    private float mCameraHeight;
    private PhysicsWorld mPhysicWorld;
    private Random random = new Random();
    private Sprite mBall;
    private float[] mGravityHist = new float[] { 0f, 0f };

    @Override
    public EngineOptions onCreateEngineOptions() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        mCameraWidth = display.getWidth();
        mCameraHeight = display.getHeight();

        mCamera = new Camera(0, 0, mCameraWidth, mCameraHeight);
        EngineOptions opts = new EngineOptions(true,
                ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(
                        mCameraWidth, mCameraHeight), mCamera);
        return opts;
    }

    @Override
    public void onCreateResources(
            OnCreateResourcesCallback pOnCreateResourcesCallback)
            throws Exception {
        BitmapTextureAtlas atlas = new BitmapTextureAtlas(getTextureManager(),
                256, 256);
        mBallTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                atlas, this, "tennis2.png", 0, 0);
        atlas.load();
        pOnCreateResourcesCallback.onCreateResourcesFinished();
        enableAccelerationSensor(this);

    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
            throws Exception {
        mScene = new Scene();
        mScene.setBackground(new RepeatingSpriteBackground(mCameraWidth,
                mCameraHeight, getTextureManager(),
                AssetBitmapTextureAtlasSource.create(getAssets(), "grass.jpg"),
                getVertexBufferObjectManager()));

        mPhysicWorld = new PhysicsWorld(new Vector2(0,
                3 * SensorManager.GRAVITY_EARTH), false);

        final float centerX = ((mCameraWidth - mBallTexture.getWidth()) / 2);
        final float centerY = ((mCameraHeight - mBallTexture.getHeight()) / 2);

        mBall = new Sprite(centerX, centerY, mBallTexture,
                getVertexBufferObjectManager());
        FixtureDef fixturedef = PhysicsFactory.createFixtureDef(1f, 0.8f, 0.5f);
        Body ballBody = PhysicsFactory.createCircleBody(mPhysicWorld, mBall,
                BodyType.DynamicBody, fixturedef);

        final VertexBufferObjectManager vertexBufferObjectManager = this
                .getVertexBufferObjectManager();
        final Rectangle ground = new Rectangle(0, mCameraHeight - 1,
                mCameraWidth, 1, vertexBufferObjectManager);
        ground.setColor(0, 0, 0);
        final Rectangle roof = new Rectangle(0, 0, mCameraWidth, 1,
                vertexBufferObjectManager);
        roof.setColor(0, 0, 0);
        final Rectangle left = new Rectangle(0, 0, 1, mCameraHeight,
                vertexBufferObjectManager);
        left.setColor(0, 0, 0);
        final Rectangle right = new Rectangle(mCameraWidth - 1, 0, 1,
                mCameraHeight, vertexBufferObjectManager);
        right.setColor(0, 0, 0);

        final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0,
                0.5f, 0.5f);
        PhysicsFactory.createBoxBody(mPhysicWorld, ground, BodyType.StaticBody,
                wallFixtureDef);
        PhysicsFactory.createBoxBody(mPhysicWorld, roof, BodyType.StaticBody,
                wallFixtureDef);
        PhysicsFactory.createBoxBody(mPhysicWorld, left, BodyType.StaticBody,
                wallFixtureDef);
        PhysicsFactory.createBoxBody(mPhysicWorld, right, BodyType.StaticBody,
                wallFixtureDef);

        this.mScene.attachChild(ground);
        this.mScene.attachChild(roof);
        this.mScene.attachChild(left);
        this.mScene.attachChild(right);

        buildTennisCourt();

        mBall.setUserData(ballBody);
        mScene.registerTouchArea(mBall);

        mScene.attachChild(mBall);
        mScene.registerUpdateHandler(mPhysicWorld);
        mPhysicWorld.registerPhysicsConnector(new PhysicsConnector(mBall,
                ballBody, true, true));
        mScene.setOnAreaTouchListener(this);

        pOnCreateSceneCallback.onCreateSceneFinished(mScene);

    }

    @Override
    public void onPopulateScene(Scene pScene,
            OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

    private void buildTennisCourt() {
        final VertexBufferObjectManager vtxm = getVertexBufferObjectManager();

        // The edge lines
        Line north = new Line(0.1f * mCameraWidth, 0.1f * mCameraHeight,
                0.9f * mCameraWidth, 0.1f * mCameraHeight, vtxm);
        north.setColor(1f, 1f, 1f);
        north.setLineWidth(5.0f);
        mScene.attachChild(north);

        Line west = new Line(0.1f * mCameraWidth, 0.1f * mCameraHeight,
                0.1f * mCameraWidth, 0.9f * mCameraHeight, vtxm);
        west.setColor(1f, 1f, 1f);
        west.setLineWidth(5.0f);
        mScene.attachChild(west);

        Line south = new Line(0.1f * mCameraWidth, 0.9f * mCameraHeight,
                0.9f * mCameraWidth, 0.9f * mCameraHeight, vtxm);
        south.setColor(1f, 1f, 1f);
        south.setLineWidth(5.0f);
        mScene.attachChild(south);

        Line east = new Line(0.9f * mCameraWidth, 0.1f * mCameraHeight,
                0.9f * mCameraWidth, 0.9f * mCameraHeight, vtxm);
        east.setColor(1f, 1f, 1f);
        east.setLineWidth(5.0f);
        mScene.attachChild(east);

        Line middle = new Line(0.1f * mCameraWidth, 0.5f * mCameraHeight,
                0.9f * mCameraWidth, 0.5f * mCameraHeight, vtxm);
        middle.setColor(1f, 1f, 1f);
        middle.setLineWidth(10.0f);
        mScene.attachChild(middle);

        Line sideWest = new Line(0.2f * mCameraWidth, 0.1f * mCameraHeight,
                0.2f * mCameraWidth, 0.9f * mCameraHeight, vtxm);
        sideWest.setColor(1f, 1f, 1f);
        sideWest.setLineWidth(5.0f);
        mScene.attachChild(sideWest);

        Line sideEast = new Line(0.8f * mCameraWidth, 0.1f * mCameraHeight,
                0.8f * mCameraWidth, 0.9f * mCameraHeight, vtxm);
        sideEast.setColor(1f, 1f, 1f);
        sideEast.setLineWidth(5.0f);
        mScene.attachChild(sideEast);

        Line sideNorth = new Line(0.2f * mCameraWidth, 0.3f * mCameraHeight,
                0.8f * mCameraWidth, 0.3f * mCameraHeight, vtxm);
        sideNorth.setColor(1f, 1f, 1f);
        sideNorth.setLineWidth(5.0f);
        mScene.attachChild(sideNorth);

        Line sideSouth = new Line(0.2f * mCameraWidth, 0.7f * mCameraHeight,
                0.8f * mCameraWidth, 0.7f * mCameraHeight, vtxm);
        sideSouth.setColor(1f, 1f, 1f);
        sideSouth.setLineWidth(5.0f);
        mScene.attachChild(sideSouth);

        Line middleMiddle = new Line(0.5f * mCameraWidth, 0.3f * mCameraHeight,
                0.5f * mCameraWidth, 0.7f * mCameraHeight, vtxm);
        middleMiddle.setColor(1f, 1f, 1f);
        middleMiddle.setLineWidth(5.0f);
        mScene.attachChild(middleMiddle);
    }

    @Override
    public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAccelerationChanged(AccelerationData pAccelerationData) {
        final float alpha = 0.6f;

        // filter gravity with a high pass filter
        mGravityHist[0] = alpha * mGravityHist[0] + (1 - alpha)
                * pAccelerationData.getX();
        mGravityHist[1] = alpha * mGravityHist[1] + (1 - alpha)
                * pAccelerationData.getY();
        float forceAccelX = pAccelerationData.getX() - mGravityHist[0];
        float forceAccelY = pAccelerationData.getY() - mGravityHist[1];

        Body body = (Body) mBall.getUserData();
        body.applyLinearImpulse(Vector2Pool.obtain(forceAccelX * body.getMass()
                * 5, forceAccelY * body.getMass() * 5), body.getWorldCenter());

        final Vector2 gravity = Vector2Pool.obtain(mGravityHist[0],
                mGravityHist[1]);
        this.mPhysicWorld.setGravity(gravity);
        Vector2Pool.recycle(gravity);

    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
            ITouchArea pTouchArea, float pTouchAreaLocalX,
            float pTouchAreaLocalY) {
        if (pSceneTouchEvent.isActionDown()) {
            final Sprite ball = (Sprite) pTouchArea;
            tap(ball);
            return true;
        }
        return false;
    }

    private void tap(Sprite ball) {
        Body body = (Body) ball.getUserData();
        float vecx = -100f + random.nextFloat() * 200f;
        float vecy = -100f + random.nextFloat() * 200f;
        final Vector2 velocity = Vector2Pool.obtain(vecx, vecy);
        body.setLinearVelocity(velocity);

        Vector2Pool.recycle(velocity);
    }

    @Override
    public void onResume() {
        // I am patching the LiveWallpaperExtension here, so you do not need to
        // change patch you copy of that project.
        // This patch on subclass will avoid a freeze after leaving the
        // lockscreen
        // refer to my post at
        // http://www.andengine.org/forums/live-wallpaper-extension/freeze-after-lock-screen-bug-in-the-extension-code-t9004.html
        if (isGameLoaded() && isGamePaused()) {
            onResumeGame();
        }
    }

}
