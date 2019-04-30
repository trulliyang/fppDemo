package com.facepp.demo.util;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class EdgeNode {

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
				+ "varying vec2 v_tc;"
				+ "void main()"
				+ "{"
				+ "    gl_Position = a_pos;"
				+ "    v_tc = a_tc;"
				+ "}";

		String fragmentShaderCode = ""
				+ "precision mediump float;"
				+ "varying vec2 v_tc;\n"
				+ "uniform sampler2D u_texture0;\n"// new face
				+ "uniform sampler2D u_texture1;\n"
				+ "uniform float u_beta;\n"
				+ "uniform float u_wStep;\n"
				+ "uniform float u_hStep;\n"
				+ "uniform float u_array50[50];\n"
				+ "uniform float u_arrayBi50[50];\n"
				+ "uniform float u_arrayBi90[90];\n"

				+ "bool needSmooth90(){    \n"
				+ "    vec2 tcStep = vec2(u_wStep, u_hStep);\n"
				+ "    float aSum = 0.0;\n"
				+ "    for (int i=0; i<45; i++) {\n"
				+ "        aSum += texture2D(u_texture0, v_tc+tcStep*vec2( u_arrayBi90[2*i], u_arrayBi90[2*i+1])).a;\n"
				+ "    }\n"
				+ "    float aMin = 1.0/45.0;"
				+ "    float aMax = 45.0 - 1.0/45.0;"
				+ "    return (aMin<=aSum) && (aSum<=aMax);\n"
				+ "    \n"
				+ "}\n"

				+ "vec4 doSmooth90(){    \n"
				+ "    vec2 tcStep = vec2(u_wStep, u_hStep);\n"
				+ "    vec4 colorSum = vec4(0.0);\n"
				+ "    vec4 color0 = vec4(0.0);\n"
                + "    vec4 color1 = vec4(0.0);\n"
				+ "    vec2 tc1 = vec2(v_tc.x, 1.0-v_tc.y);\n"
                + "    for (int i=0; i<45; i++) {\n"
				+ "        color0 = texture2D(u_texture0, v_tc+tcStep*vec2( u_arrayBi90[2*i], u_arrayBi90[2*i+1]));\n"
				+ "        color1 = texture2D(u_texture1, tc1);\n"
//				+ "        color0 = vec4(vec3(0.0), color0.a);\n"
//				+ "        color1 = vec4(vec3(1.0), 1.0);\n"
				+ "        colorSum += mix(color1, color0, color0.a);\n"
                + "    }\n"
				+ "    return colorSum/45.0;\n"
				+ "    \n"
				+ "    \n"
				+ "}\n"


				+ "bool needSmooth50(){    \n"
				+ "    vec2 tcStep = vec2(u_wStep, u_hStep);\n"
				+ "    float aSum = 0.0;\n"
				+ "    for (int i=0; i<25; i++) {\n"
				+ "        aSum += texture2D(u_texture0, v_tc+tcStep*vec2( u_arrayBi50[2*i], u_arrayBi50[2*i+1])).a;\n"
				+ "    }\n"
				+ "    float aMin = 1.0/25.0;"
				+ "    float aMax = 25.0 - 1.0/25.0;"
				+ "    return (aMin<=aSum) && (aSum<=aMax);\n"
				+ "    \n"
				+ "}\n"

				+ "vec4 doSmooth50(){    \n"
				+ "    vec2 tcStep = vec2(u_wStep, u_hStep);\n"
				+ "    vec4 colorSum = vec4(0.0);\n"
				+ "    vec4 color0 = vec4(0.0);\n"
				+ "    vec4 color1 = vec4(0.0);\n"
				+ "    vec2 tc1 = vec2(v_tc.x, 1.0-v_tc.y);\n"
				+ "    for (int i=0; i<25; i++) {\n"
				+ "        color0 = texture2D(u_texture0, v_tc+tcStep*vec2( u_arrayBi50[2*i], u_arrayBi50[2*i+1]));\n"
				+ "        color1 = texture2D(u_texture1, tc1);\n"
//				+ "        color0 = vec4(vec3(0.0), color0.a);\n"
//				+ "        color1 = vec4(vec3(1.0), 1.0);\n"
				+ "        colorSum += mix(color1, color0, color0.a);\n"
				+ "    }\n"
				+ "    return colorSum/25.0;\n"
				+ "    \n"
				+ "    \n"
				+ "}\n"

				+ "vec4 doBlending(vec4 bottom, vec4 top){    \n"
				+ "    vec4 result = vec4(1.0);\n"

                + "    if (bottom.r < 0.5) result.r = bottom.r*top.r;\n"
				+ "    else result.r = 1.0-2.0*(1.0-bottom.r)*(1.0-top.r);\n"

				+ "    if (bottom.g < 0.5) result.g = bottom.g*top.g;\n"
				+ "    else result.g = 1.0-2.0*(1.0-bottom.g)*(1.0-top.g);\n"

				+ "    if (bottom.b < 0.5) result.b = bottom.b*top.b;\n"
				+ "    else result.b = 1.0-2.0*(1.0-bottom.b)*(1.0-top.b);\n"

				+ "    return result;\n"
				+ "}\n"

				+ "vec4 doBlending2(vec4 bottom, vec4 top){    \n"
				+ "    vec4 result = vec4(1.0);\n"

//				+ "    if (bottom.r < 0.5) result.r = bottom.r*top.r;\n"
				+ "    result.r = 1.0-(1.0-bottom.r)*(1.0-top.r);\n"

//				+ "    if (bottom.g < 0.5) result.g = bottom.g*top.g;\n"
				+ "    result.g = 1.0-(1.0-bottom.g)*(1.0-top.g);\n"

//				+ "    if (bottom.b < 0.5) result.b = bottom.b*top.b;\n"
				+ "    result.b = 1.0-(1.0-bottom.b)*(1.0-top.b);\n"

				+ "    return result;\n"
				+ "}\n"


				+ "void main() {\n"
				+ "    vec4 color1 = texture2D(u_texture1, vec2(v_tc.x, 1.0-v_tc.y));\n"
				+ "    vec4 color0 = texture2D(u_texture0, v_tc );\n"
				+ "    vec2 tcStep = vec2(u_wStep, u_hStep);\n"
				+ "    vec4 finalColor = vec4(1.0, 0.0, 0.0, 1.0);\n"
//		        + "    finalColor =  mix(color1, color0, color0.a);\n"
//				+ "    if (needSmooth50()) {\n"
//				+ "        finalColor =  vec4(0.0, 0.0, 1.0, 1.0);\n"
//				+ "        finalColor =  doSmooth50();\n"
//				+ "    }\n"

				+ "    finalColor =  color0.a > 0.0 ? doBlending2(color1, color0) : color1;\n"
				+ "    gl_FragColor = finalColor;\n"
//				+ "    float aSum = 0.0;\n"
//				+ "    for (int i=0; i<25; i++) {\n"
//				+ "        aSum += texture2D(u_texture0, v_tc + tcStep*vec2( u_array50[2*i], u_array50[2*i+1]) ).a;\n"
//				+ "    }\n"
//				+ "    float aAvg = clamp(aSum*0.04, 0.1, 1.0);\n"
//				+ "    float alpha = aAvg*u_beta;\n"
//				+ "    alpha *= color.a;\n"
//				+ "    vec3 miixx = mix(color1.rgb, color.rgb, alpha*u_beta);\n"
//				+ "    miixx = clamp(miixx, vec3(0.0), vec3(1.0));\n"
//
//				+ "    gl_FragColor = vec4(miixx, 1.0);\n"
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

	public EdgeNode(Context ctx) {
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

		loc = GLES20.glGetUniformLocation(mProgram, "u_wStep");
		GLES20.glUniform1f(loc, 1.0f / w);
		loc = GLES20.glGetUniformLocation(mProgram, "u_hStep");
		GLES20.glUniform1f(loc, 1.0f / h);

		loc = GLES20.glGetUniformLocation(mProgram, "u_beta");
		GLES20.glUniform1f(loc, 1.0f);

		loc = GLES20.glGetUniformLocation(mProgram, "u_array50");
		GLES20.glUniform1fv(loc, mFloatBuffer50.capacity(), mFloatBuffer50);

		loc = GLES20.glGetUniformLocation(mProgram, "u_arrayBi50");
		GLES20.glUniform1fv(loc, mFloatBufferBi50.capacity(), mFloatBufferBi50);

		loc = GLES20.glGetUniformLocation(mProgram, "u_arrayBi90");
		GLES20.glUniform1fv(loc, mFloatBufferBi50.capacity(), mFloatBufferBi90);


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