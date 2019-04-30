package com.facepp.demo.util;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

public class SkinColorNode {
    private float[] mLabMeanVideo = new float[3];
	private float[] mLabStdDevVideo = new float[3];
	private float[] mLabMeanUser = new float[3];
	private float[] mLabStdDevUser = new float[3];

	private int mTransferMode = 1;

	private float[] mArray50 = new float[]{
			-2f,  2f, -1f,  2f, 0f,  2f, 1f,  2f, 2f,  2f,
			-2f,  1f, -1f,  1f, 0f,  1f, 1f,  1f, 2f,  1f,
			-2f,  0f, -1f,  0f, 0f,  0f, 1f,  0f, 2f,  0f,
			-2f, -1f, -1f, -1f, 0f, -1f, 1f, -1f, 2f, -1f,
			-2f, -2f, -1f, -2f, 0f, -2f, 1f, -2f, 2f, -2f,
	};

//	private float[] mArrayBi50 = new float[]{
//			                              0f,  3f,
//			                    -1f,  2f, 0f,  2f, 1f,  2f,
//			          -2f,  1f, -1f,  1f, 0f,  1f, 1f,  1f, 2f,  1f,
//			-3f,  0f, -2f,  0f, -1f,  0f, 0f,  0f, 1f,  0f, 2f,  0f, 3f, 0f,
//			          -2f, -1f, -1f, -1f, 0f, -1f, 1f, -1f, 2f, -1f,
//			                    -1f, -2f, 0f, -2f, 1f, -2f,
//			                              0f, -3f
//	};

	private float[] mArrayBi50 = new float[]{
			                                 0f,10f,
			                                 0f,7f,
			             -5f,5f,                              5f,5f,
			                                 0f,4f,
			                     -2f,2f,               2f,2f,
			                                 0f,1f,
			-10f,0f, -7f,0f, -4f,0f, -1f,0f, 0f,0f, 1f,0f, 4f,0f, 7f,0f, 10f,0f,
			                                 0f,-1f,
			                     -2f,-2f,              2f,-2f,
			                                 0f,-4f,
			             -5f,-5f,                             5f,-5f,
			                                 0f,-7f,
			                                 0f,-10f
	};


	private float[] mArrayBi90 = new float[]{
			                                        0f,5f,
                                            -1f,4f, 0f,4f, 1f,4f,
                                    -2f,3f,         0f,3f,        2f,3f,
			                -3f,2f,         -1f,2f, 0f,2f, 1f,2f,        3f,2f,
                    -4f,1f          -2f,1f,         0f,1f,        2f,1f,        4f,1f,
			-5f,0f, -4f,0f, -3f,0f, -2f,0f, -1f,0f, 0f,0f, 1f,0f, 2f,0f, 3f,0f, 4f,0f, 5f,0f,
			        -4f,-1f         -2f,-1f,        0f,-1f,       2f,-1f,       4f,-1f,
			                -3f,-2f,        -1f,-2f,0f,-2f,1f,-2f,       3f,-2f,
			                        -2f,-3f,        0f,-3f,       2f,-3f,
			                                -1f,-4f,0f,-4f,1f,-4f,
			                                        0f,-5f
	};




	private FloatBuffer mFloatBuffer50;
	private FloatBuffer mFloatBufferBi50;
	private FloatBuffer mFloatBufferBi90;

	private int mProgram;

//	private int mWidthUsers = 1280;
//	private int mHeightUsers = 720;
//	private int mWidthStars = 1280;
//	private int mHeightStars = 720;


	private ShortBuffer idxBuffer;

	private int[] m_vtxBufId = new int[1];
	private int[] m_texBufId = new int[1];
	private int[] m_idxBufId = new int[1];

	private void initMesh() {
		Log.e("shiyang", "shiyang initMesh");
		float[] t_v = new float[]{
				-1f,  1f,
				 1f,  1f,
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

	private void initShader() {
		// vertex着色器code
		String vertexShaderCode = ""
				+ "attribute vec4 a_pos;"
				+ "attribute vec2 a_tc;"
				+ "varying vec2 v_tc0;"
				+ "varying vec2 v_tc1;"
				+ "void main()"
				+ "{"
				+ "    gl_Position = a_pos;"
				+ "    v_tc0 = a_tc;"
				+ "    v_tc1 = vec2(a_tc.x, 1.0-a_tc.y);"
				+ "}";

		String fragmentShaderCode = ""
				+ "precision mediump float;"
				+ "varying vec2 v_tc0;\n"
				+ "varying vec2 v_tc1;\n"
				+ "uniform sampler2D u_texture0;\n"// new face
				+ "uniform sampler2D u_texture1;\n"// video

				+ "uniform int u_transferMode;\n"

				+ "uniform float u_lVideoMean;\n"
				+ "uniform float u_aVideoMean;\n"
				+ "uniform float u_bVideoMean;\n"

				+ "uniform float u_lVideoStdDev;\n"
				+ "uniform float u_aVideoStdDev;\n"
				+ "uniform float u_bVideoStdDev;\n"

				+ "uniform float u_lUserMean;\n"
				+ "uniform float u_aUserMean;\n"
				+ "uniform float u_bUserMean;\n"

				+ "uniform float u_lUserStdDev;\n"
				+ "uniform float u_aUserStdDev;\n"
				+ "uniform float u_bUserStdDev;\n"

				+ "vec3 rgb2lab(vec3 rgb) {\n"
				+ "   vec3 lab = vec3(0.0);\n"
				+ "   float R = rgb.r;\n"
				+ "   float G = rgb.g;\n"
				+ "   float B = rgb.b;\n"
				+ "   float X= (0.412453*R + 0.357580*G + 0.180423*B);\n"
				+ "   float Y= (0.212671*R + 0.715160*G + 0.072169*B);\n"
				+ "   float Z= (0.019334*R + 0.119193*G + 0.950227*B);\n"

				+ "   X /= 0.950456;\n"

				+ "   Z /= 1.088754;\n"

				+ "   float fX,fY,fZ;\n"
				+ "   if (Y > 0.008856)\n"
				+ "       fY = pow(Y, 1.0/3.0);\n"
				+ "   else\n"
				+ "       fY = 7.787 * Y + 16.0/116.0;\n"

				+ "   if (X > 0.008856)\n"
				+ "       fX = pow(X, 1.0/3.0);\n"
				+ "   else\n"
				+ "       fX = 7.787 * X + 16.0/116.0;\n"

				+ "   if (Z > 0.008856)\n"
				+ "       fZ = pow(Z, 1.0/3.0);\n"
				+ "   else\n"
				+ "       fZ = 7.787 * Z + 16.0/116.0;\n"

				+ "   lab.r = clamp(116.0 * fY - 16.0,    0.0, 100.0);\n"
				+ "   lab.g = clamp(500.0 * (fX - fY), -128.0, 127.0);\n"
				+ "   lab.b = clamp(200.0 * (fY - fZ), -128.0, 127.0);\n"

                + "   return lab;\n"
				+ "}\n"


				+ "vec3 lab2rgb(vec3 lab) {\n"
				+ "   float param_16116 = 16.0 / 116.0;\n"
				+ "   vec3 rgb = vec3(0.0);\n"
				+ "   float fX, fY, fZ;\n"
				+ "   float X, Y, Z;\n"
				+ "   float L = lab.r;\n"
				+ "   float a = lab.g;\n"
				+ "   float b = lab.b;\n"

				+ "   fY = (L + 16.0) / 116.0;\n"
				+ "   if (fY > 0.206893)\n"
				+ "       Y = fY * fY * fY;\n"
				+ "   else\n"
				+ "       Y = (fY-param_16116) / 7.787;\n"

				+ "   fX = a / 500.0 + fY;\n"
				+ "   if (fX > 0.206893)\n"
				+ "       X = fX * fX * fX;\n"
				+ "   else\n"
				+ "       X = (fX-param_16116) / 7.787;\n"

				+ "   fZ = fY - b / 200.0;\n"
				+ "   if (fZ > 0.206893)\n"
				+ "       Z = fZ * fZ * fZ;\n"
				+ "   else\n"
				+ "       Z = (fZ -param_16116) / 7.787;\n"

				+ "   X *= (0.950456*255.0);\n"
				+ "   Y *= 255.0;\n"
				+ "   Z *= (1.08875*255.0);\n"

				+ "   float RR, GG, BB;\n"
				+ "   RR = 3.240479 * X - 1.537150 * Y - 0.498535 * Z;\n"
				+ "   GG = -0.969256 * X + 1.875992 * Y + 0.041556 * Z;\n"
				+ "   BB = 0.055648 * X - 0.204043 * Y + 1.057311 * Z;\n"
		        + "   rgb = vec3(RR, GG, BB)/255.0;\n"
				+ "   rgb = clamp(rgb, vec3(0.0), vec3(1.0));\n"
				+ "   return rgb;\n"
				+ "}\n"

//		l -= lMeanTar
//		a -= aMeanTar
//		b -= bMeanTar
//
//	# scale by the standard deviations
//				l = (lStdTar / lStdSrc) * l
//		a = (aStdTar / aStdSrc) * a
//		b = (bStdTar / bStdSrc) * b
//
//	# add in the source mean
//		l += lMeanSrc
//		a += aMeanSrc
//		b += bMeanSrc

				+ "vec4 doTransfer0(vec4 rgba0, vec4 rgba1) {\n"
				+ "   vec4 labT = vec4(1.0);\n"
				+ "   vec3 lab0 = rgb2lab(rgba0.rgb);\n"
				+ "   vec3 lab1 = rgb2lab(rgba1.rgb);\n"

				+ "   lab0.r -= u_lUserMean;\n"
				+ "   lab0.g -= u_aUserMean;\n"
				+ "   lab0.b -= u_bUserMean;\n"

				+ "   lab0.r *= u_lUserStdDev/u_lVideoStdDev;\n"
				+ "   lab0.g *= u_aUserStdDev/u_aVideoStdDev;\n"
				+ "   lab0.b *= u_bUserStdDev/u_bVideoStdDev;\n"

				+ "   lab0.r += u_lVideoMean;\n"
				+ "   lab0.g += u_aVideoMean;\n"
				+ "   lab0.b += u_bVideoMean;\n"

				+ "   lab0 = clamp(lab0, vec3(0.0, -128.0, -128.0), vec3(100.0, 127.0, 127.0));\n"
                + "   labT.rgb = lab2rgb(lab0);\n"

				+ "   return labT;\n"
				+ "}\n"

				+ "vec4 doTransfer1(vec4 rgba0, vec4 rgba1) {\n"
				+ "   vec4 labT = vec4(1.0);\n"
				+ "   vec3 lab0 = rgb2lab(rgba0.rgb);\n"
				+ "   vec3 lab1 = rgb2lab(rgba1.rgb);\n"

				+ "   lab0.r -= u_lUserMean;\n"
				+ "   lab0.g -= u_aUserMean;\n"
				+ "   lab0.b -= u_bUserMean;\n"

//				+ "   lab0.r *= u_lUserStdDev/u_lVideoStdDev;\n"
//				+ "   lab0.g *= u_aUserStdDev/u_aVideoStdDev;\n"
//				+ "   lab0.b *= u_bUserStdDev/u_bVideoStdDev;\n"

				+ "   lab0.r += u_lVideoMean;\n"
				+ "   lab0.g += u_aVideoMean;\n"
				+ "   lab0.b += u_bVideoMean;\n"

				+ "   lab0 = clamp(lab0, vec3(0.0, -128.0, -128.0), vec3(100.0, 127.0, 127.0));\n"
				+ "   labT.rgb = lab2rgb(lab0);\n"

				+ "   return labT;\n"
				+ "}\n"

				+ "vec4 doTransfer2(vec4 rgba0, vec4 rgba1) {\n"
				+ "   vec4 labT = vec4(1.0);\n"
				+ "   vec3 lab0 = rgb2lab(rgba0.rgb);\n"
				+ "   vec3 lab1 = rgb2lab(rgba1.rgb);\n"

				+ "   lab0.r -= u_lUserMean;\n"
				+ "   lab0.g -= u_aUserMean;\n"
				+ "   lab0.b -= u_bUserMean;\n"

				+ "   float alpha = 0.5;\n"
				+ "   lab0.r *= mix(u_lUserMean/u_lVideoMean, u_lUserStdDev/u_lVideoStdDev, alpha);\n"
				+ "   lab0.g *= mix(u_aUserMean/u_aVideoMean, u_aUserStdDev/u_aVideoStdDev, alpha);\n"
				+ "   lab0.b *= mix(u_bUserMean/u_bVideoMean, u_bUserStdDev/u_bVideoStdDev, alpha);\n"

				+ "   lab0.r += u_lVideoMean;\n"
				+ "   lab0.g += u_aVideoMean;\n"
				+ "   lab0.b += u_bVideoMean;\n"

//				+ "   lab0.r = lab1.r;\n"
//				+ "   lab0.g = lab1.g;\n"
//				+ "   lab0.b = lab1.b;\n"

				+ "   lab0 = clamp(lab0, vec3(0.0, -128.0, -128.0), vec3(100.0, 127.0, 127.0));\n"
				+ "   labT.rgb = lab2rgb(lab0);\n"

				+ "   return labT;\n"
				+ "}\n"

                + "vec4 doTransfer3(vec4 rgba0, vec4 rgba1) {\n"
                + "   vec4 labT = vec4(1.0);\n"
                + "   vec3 lab0 = rgb2lab(rgba0.rgb);\n"
                + "   vec3 lab1 = rgb2lab(rgba1.rgb);\n"
                + "   float dis = distance(lab0, lab1);\n"
				+ "   dis = dis / 5.0;\n"

				+ "   float f = 1.0 / exp(3.0 * dis * dis);\n"
				+ "   lab0.r = lab0.r + f * (lab1.r - u_lVideoMean);\n"
				+ "   lab0.g = lab0.g + f * (lab1.g - u_aVideoMean);\n"
				+ "   lab0.b = lab0.b + f * (lab1.b - u_bVideoMean);\n"

                + "   lab0 = clamp(lab0, vec3(0.0, -128.0, -128.0), vec3(100.0, 127.0, 127.0));\n"
                + "   labT.rgb = lab2rgb(lab0);\n"

                + "   return labT;\n"
                + "}\n"

				+ "void main() {\n"
				+ "    vec4 color0 = texture2D(u_texture0, v_tc0);\n"
				+ "    vec4 color1 = texture2D(u_texture1, v_tc1);\n"
				+ "    vec4 finalColor = vec4(1.0, 0.0, 0.0, 1.0);\n"
				+ "    if (color0.a == 0.0) {\n"
				+ "        finalColor =  color1;\n"
				+ "    } else if (color0.a < 1.0) {\n"
				+ "        vec4 color2 = clamp(color0/color0.a, vec4(0.0), vec4(1.0));\n"
				+ "        finalColor =  mix(color1, color2, color0.a);\n"
				+ "    } else {\n"
				+ "        vec4 color2 = clamp(color0, vec4(0.0), vec4(1.0));\n"
				+ "        if (0 == u_transferMode) {\n"
		        + "            finalColor =  doTransfer0(color2, color1);\n"
				+ "        } else if (1 == u_transferMode) {\n"
				+ "            finalColor =  doTransfer1(color2, color1);\n"
				+ "        } else if (2 == u_transferMode) {\n"
				+ "            finalColor =  doTransfer2(color2, color1);\n"
				+ "        } else if (3 == u_transferMode) {\n"
				+ "            finalColor =  doTransfer3(color2, color1);\n"
				+ "        }\n"
//				+ "        finalColor =  finalColor;\n"
				+ "    }\n"

				+ "    gl_FragColor = finalColor;\n"
				+ "}\n";

		mProgram = GLES20.glCreateProgram();

		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		GLES20.glAttachShader(mProgram, vertexShader);


		int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
		GLES20.glAttachShader(mProgram, fragmentShader);

		GLES20.glLinkProgram(mProgram);
	}

    private void initNio() {
		ByteBuffer cc = ByteBuffer.allocateDirect(mArray50.length * 4);
		cc.order(ByteOrder.nativeOrder());
		mFloatBuffer50 = cc.asFloatBuffer();
		mFloatBuffer50.put(mArray50);
		mFloatBuffer50.position(0);

		ByteBuffer ccbi = ByteBuffer.allocateDirect(mArrayBi50.length * 4);
		ccbi.order(ByteOrder.nativeOrder());
		mFloatBufferBi50 = ccbi.asFloatBuffer();
		mFloatBufferBi50.put(mArrayBi50);
		mFloatBufferBi50.position(0);

		ByteBuffer ccbi90 = ByteBuffer.allocateDirect(mArrayBi90.length * 4);
		ccbi90.order(ByteOrder.nativeOrder());
		mFloatBufferBi90 = ccbi90.asFloatBuffer();
		mFloatBufferBi90.put(mArrayBi90);
		mFloatBufferBi90.position(0);
	}

	public void init() {
		initMesh();
		initShader();
        initNio();
	}

	public SkinColorNode(Context ctx) {
        init();
	}

	public void draw(int id0, int id1, int w, int h) {
//		GLES20.glClearColor(0,0,0,0);
//		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		GLES20.glUseProgram(mProgram);

		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id0);
		int loc = GLES20.glGetUniformLocation(mProgram, "u_texture0");
		GLES20.glUniform1i(loc, 0);

		GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id1);
		loc = GLES20.glGetUniformLocation(mProgram, "u_texture1");
		GLES20.glUniform1i(loc, 1);

		loc = GLES20.glGetUniformLocation(mProgram, "u_transferMode");
		GLES20.glUniform1i(loc, mTransferMode);
//		Log.e("shiyang", "shiyang mode="+mTransferMode);

		loc = GLES20.glGetUniformLocation(mProgram, "u_lVideoMean");
		GLES20.glUniform1f(loc, mLabMeanVideo[0]);
		loc = GLES20.glGetUniformLocation(mProgram, "u_aVideoMean");
		GLES20.glUniform1f(loc, mLabMeanVideo[1]);
		loc = GLES20.glGetUniformLocation(mProgram, "u_bVideoMean");
		GLES20.glUniform1f(loc, mLabMeanVideo[2]);

		loc = GLES20.glGetUniformLocation(mProgram, "u_lVideoStdDev");
		GLES20.glUniform1f(loc, mLabStdDevVideo[0]);
		loc = GLES20.glGetUniformLocation(mProgram, "u_aVideoStdDev");
		GLES20.glUniform1f(loc, mLabStdDevVideo[1]);
		loc = GLES20.glGetUniformLocation(mProgram, "u_bVideoStdDev");
		GLES20.glUniform1f(loc, mLabStdDevVideo[2]);

		loc = GLES20.glGetUniformLocation(mProgram, "u_lUserMean");
		GLES20.glUniform1f(loc, mLabMeanUser[0]);
		loc = GLES20.glGetUniformLocation(mProgram, "u_aUserMean");
		GLES20.glUniform1f(loc, mLabMeanUser[1]);
		loc = GLES20.glGetUniformLocation(mProgram, "u_bUserMean");
		GLES20.glUniform1f(loc, mLabMeanUser[2]);

		loc = GLES20.glGetUniformLocation(mProgram, "u_lUserStdDev");
		GLES20.glUniform1f(loc, mLabStdDevUser[0]);
		loc = GLES20.glGetUniformLocation(mProgram, "u_aUserStdDev");
		GLES20.glUniform1f(loc, mLabStdDevUser[1]);
		loc = GLES20.glGetUniformLocation(mProgram, "u_bUserStdDev");
		GLES20.glUniform1f(loc, mLabStdDevUser[2]);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, m_vtxBufId[0]);
		int vcLoc = GLES20.glGetAttribLocation(mProgram, "a_pos");
		GLES20.glEnableVertexAttribArray(vcLoc);
		GLES20.glVertexAttribPointer(vcLoc, 2, GLES20.GL_FLOAT, false, 0, 0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, m_texBufId[0]);
		int tcLoc = GLES20.glGetAttribLocation(mProgram, "a_tc");
		GLES20.glEnableVertexAttribArray(tcLoc);
		GLES20.glVertexAttribPointer(tcLoc, 2, GLES20.GL_FLOAT, false, 0, 0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, m_idxBufId[0]);
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, idxBuffer.capacity(), GLES20.GL_UNSIGNED_SHORT, 0);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);

		GLES20.glDisableVertexAttribArray(vcLoc);
		GLES20.glDisableVertexAttribArray(tcLoc);
		GLES20.glDisable(GLES20.GL_BLEND);
	}


	public void setTransferMode(int m) {

		mTransferMode = m;
	}

	public void setSkinInfos(SkinInfo videoInfo, SkinInfo userInfo) {
		setSkinInfos(videoInfo.getLabMean(), videoInfo.getLabStandardDeviation(), userInfo.getLabMean(), userInfo.getLabStandardDeviation());
	}

	public void setSkinInfos(float[] labMeanVideo, float[] labStdDevVideo, float[] labMeanUser, float[] labStdDevUser) {
		mLabMeanVideo[0] = labMeanVideo[0];
		mLabMeanVideo[1] = labMeanVideo[1];
		mLabMeanVideo[2] = labMeanVideo[2];

		mLabStdDevVideo[0] = labStdDevVideo[0];
		mLabStdDevVideo[1] = labStdDevVideo[1];
		mLabStdDevVideo[2] = labStdDevVideo[2];

		mLabMeanUser[0] = labMeanUser[0];
		mLabMeanUser[1] = labMeanUser[1];
		mLabMeanUser[2] = labMeanUser[2];

		mLabStdDevUser[0] = labStdDevUser[0];
		mLabStdDevUser[1] = labStdDevUser[1];
		mLabStdDevUser[2] = labStdDevUser[2];

//		mLabMeanVideo[0] /= 255.0f;
//		mLabMeanVideo[1] /= 255.0f;
//		mLabMeanVideo[2] /= 255.0f;
//
//		mLabStdDevVideo[0] /= 255.0f;
//		mLabStdDevVideo[1] /= 255.0f;
//		mLabStdDevVideo[2] /= 255.0f;
//
//		mLabMeanUser[0] /= 255.0f;
//		mLabMeanUser[1] /= 255.0f;
//		mLabMeanUser[2] /= 255.0f;
//
//		mLabStdDevUser[0] /= 255.0f;
//		mLabStdDevUser[1] /= 255.0f;
//		mLabStdDevUser[2] /= 255.0f;

//		Log.e("shiyang", "shiyang lab v="+mLabMeanVideo[0]+","+mLabMeanVideo[1]+","+mLabMeanVideo[2]+","+mLabStdDevVideo[0]+","+mLabStdDevVideo[1]+","+mLabStdDevVideo[2]);
//		Log.e("shiyang", "shiyang lab u="+mLabMeanUser[0]+","+mLabMeanUser[1]+","+mLabMeanUser[2]+","+mLabStdDevUser[0]+","+mLabStdDevUser[1]+","+mLabStdDevUser[2]);


	}


	/**
	 * 加载 著色器
	 */
	private int loadShader(int type, String shaderCode) {
		int shader = GLES20.glCreateShader(type);

		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;
	}

}