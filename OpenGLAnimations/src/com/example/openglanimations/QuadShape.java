package com.example.openglanimations;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class QuadShape {
    
    private float x;
    private float y;
    private float z;
    private float Rx;
    private float Ry;
    private float Rz;
    //private int width;
    //private int height;
    
    private FloatBuffer vertexBuffer;
    
    private float vertices[] = {
            -1.0f, -1.0f, 0.0f, // Bottom Left
            1.0f, -1.0f, 0.0f, // Bottom Right
            -1.0f, 1.0f, 0.0f, // Top Left
            1.0f, 1.0f, 0.0f   // Top Right
    };
    
    public QuadShape(){
        this(0f,0f,0f,0f,0f,0f);
    }
    
    public QuadShape(float x, float y, float z, float Rx, float Ry, float Rz/*, int width, int height*/){
        this.x = x;
        this.y = y;
        this.z = z;
        this.Rx = Rx;
        this.Ry = Ry;
        this.Rz = Rz;
        //this.width = width;
        //this.height = height;
        
        
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuf.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }
    
    public void setX(float x){
        this.x = x;
    }
    
    public void setY(float y){
        this.y = y;
    }
    
    public void setZ(float z){
        this.z = z;
    }
    
    public void setRx(float rx) {
        Rx = rx;
    }

    public void setRy(float ry) {
        Ry = ry;
    }

    public void setRz(float rz) {
        Rz = rz;
    }

    public void draw(GL10 gl) {
        
        gl.glPushMatrix();

            gl.glTranslatef(x, y, z);
        
            gl.glRotatef(Rx, 1f, 0f, 0f);
            gl.glRotatef(Ry, 0f, 1f, 0f);
            gl.glRotatef(Rz, 0f, 0f, 1f);

            //Set the face rotation
            gl.glFrontFace(GL10.GL_CW);
            
            //Point to our vertex buffer
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
            
            //Enable vertex buffer
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            
            //Draw the vertices as triangle strip
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
            
            //Disable the client state before leaving
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
         
         gl.glPopMatrix();
    }

}
