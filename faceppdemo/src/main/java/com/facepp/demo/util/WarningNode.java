package com.facepp.demo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.util.Log;

import com.facepp.demo.R;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class WarningNode {


	private int mProgram;

    private int mTexture0ID = -999;

	private ShortBuffer idxBuffer;

	private int[] m_vtxBufId = new int[1];
	private int[] m_texBufId = new int[1];
	private int[] m_idxBufId = new int[1];

	private void initMesh() {
		Log.e("shiyang", "shiyang initMesh");
		float[] t_v = new float[]{
				-1f, 1f,
				1f, 1f,
				1f, -1f,
				-1f, -1f
		};

		ByteBuffer bb = ByteBuffer.allocateDirect(t_v.length * 4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer vtxBuffer = bb.asFloatBuffer();
		vtxBuffer.put(t_v);
		vtxBuffer.position(0);

		GLES20.glGenBuffers(1, m_vtxBufId, 0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, m_vtxBufId[0]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vtxBuffer.capacity() * 4, vtxBuffer, GLES20.GL_STATIC_DRAW);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

		float[] t_t = new float[]{
				0f, 1f,
				1f, 1f,
				1f, 0f,
				0f, 0f
		};

		ByteBuffer cc = ByteBuffer.allocateDirect(t_t.length * 4);
		cc.order(ByteOrder.nativeOrder());
		FloatBuffer texBuffer = cc.asFloatBuffer();
		texBuffer.put(t_t);
		texBuffer.position(0);

		GLES20.glGenBuffers(1, m_texBufId, 0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, m_texBufId[0]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, texBuffer.capacity() * 4, texBuffer, GLES20.GL_STATIC_DRAW);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

		short[] t_i = new short[]{
				0, 1, 2,
				2, 3, 0
		};

		ByteBuffer dd = ByteBuffer.allocateDirect(t_i.length * 2);
		dd.order(ByteOrder.nativeOrder());
		idxBuffer = dd.asShortBuffer();
		idxBuffer.put(t_i);
		idxBuffer.position(0);

		GLES20.glGenBuffers(1, m_idxBufId, 0);
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, m_idxBufId[0]);
		GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, idxBuffer.capacity() * 2, idxBuffer, GLES20.GL_STATIC_DRAW);
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public WarningNode(Context ctx) {
		initMesh();
		mProgram = GLES20.glCreateProgram(); // create empty OpenGL ES Program

		// 拿出两个着色器 顶点着色器和碎片着色器
		// vertex着色器code
		String vs_str = ""
				+ "attribute vec4 vPosition;"
//				+ "attribute vec2 inputTextureCoordinate;"
//				+ "varying vec2 textureCoordinate;"
				+ "void main()"
				+ "{"
				+ "    gl_Position = vPosition;"
//				+ "    textureCoordinate = inputTextureCoordinate;"
				+ "}";
		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vs_str);
		GLES20.glAttachShader(mProgram, vertexShader); // add the vertex shader

		// fragment着色器code
		String fs_str = ""
				+ "precision mediump float;"
//				+ "varying vec2 textureCoordinate;\n"
//				+ "uniform sampler2D u_texture0;\n"
				+ "void main() {"
				+ "    vec2 tc = textureCoordinate;"
				+ "    vec4 color = vec4(0.0);\n"
				+ "    if (tc.x<=0.06 || tc.y <= 0.06 || tc.x>=0.94 || tc.y>=0.94) {\n"
				+ "        color = vec4(1.0, 0.0, 0.0, 1.0);\n"
				+ "    }\n"
				+ "    gl_FragColor = color.rgba;\n"
				+ "}";
		int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fs_str);
		GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment
		// shader to program
		GLES20.glLinkProgram(mProgram); // creates OpenGL ES program executables

//		openPicture(ctx);
	}

	private void openPicture(Context ctx) {
		InputStream is = ctx.getResources().openRawResource(R.raw.nf);
		Bitmap bitmap = BitmapFactory.decodeStream(is);
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
//		Bitmap.Config cfg =  bitmap.getConfig();
		int[] ia = new int[w*h];
		bitmap.getPixels(ia, 0, w,0,0, w, h);

		ByteBuffer pixels = ByteBuffer.allocate(w*h*4);
		pixels.order(ByteOrder.nativeOrder());
		pixels.asIntBuffer().put(ia);

		Log.e("shiyang", "shiyang bitmap (w,h)=("+w+","+h+")");
		int[] tex = new int[1];
		GLES20.glGenTextures(1, tex, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex[0]);

		// Set filtering
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

		// Load the bitmap into the bound texture.
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, w, h,0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, pixels);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		// Recycle the bitmap, since its data has been loaded into OpenGL.
		bitmap.recycle();
		this.mTexture0ID =  tex[0];
	}

	public void draw() {
//		GLES20.glClearColor(0,0,0,0);
//		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		GLES20.glUseProgram(mProgram);

		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

//		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
//		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture0ID);
//		int loc = GLES20.glGetUniformLocation(mProgram, "u_texture0");
//		GLES20.glUniform1i(loc, 0);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, m_vtxBufId[0]);
		int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(mPositionHandle, 2, GLES20.GL_FLOAT, false, 0, 0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, m_texBufId[0]);
		int mTextureCoordHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");
		GLES20.glEnableVertexAttribArray(mTextureCoordHandle);
		GLES20.glVertexAttribPointer(mTextureCoordHandle, 2, GLES20.GL_FLOAT, false, 0, 0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, m_idxBufId[0]);
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, idxBuffer.capacity(), GLES20.GL_UNSIGNED_SHORT, 0);

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);

		GLES20.glDisableVertexAttribArray(mPositionHandle);
		GLES20.glDisableVertexAttribArray(mTextureCoordHandle);
		GLES20.glDisable(GLES20.GL_BLEND);
	}

	/**
	 * 加载 著色器
	 */
	private int loadShader(int type, String shaderCode) {
		// create a vertex shader type (GLES20.GL_VERTEX_SHADER)
		// or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(type);

		// add the source code to the shader and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;
	}
}