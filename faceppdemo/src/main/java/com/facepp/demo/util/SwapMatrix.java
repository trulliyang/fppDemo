package com.facepp.demo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import com.facepp.demo.R;

import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ConcurrentModificationException;

import static com.facepp.demo.util.ColorUtil.getSkinInfo;

public class SwapMatrix {
	// 157*3 = 468
	private short[] indexArray = new short[] {
			33,34,64,//0
			64,34,65,
			65,34,35,
			65,35,36,
			66,65,36,
			66,36,67,
			36,37,67,
			67,37,38,//7
			67,38,68,
			68,38,39,//9
			69,68,39,
			69,39,70,
			70,39,40,
			70,40,41,
			70,41,71,
			71,41,42,
			33,64,52,//16
			64,53,52,
			64,65,53,
			53,65,72,
			72,65,66,
			72,66,54,
			54,66,55,
			66,55,67,//23
			55,67,78,
			67,78,43,
			67,43,68,
			43,68,79,
			68,79,58,
			68,58,69,
			58,69,59,//30
			69,59,75,
			69,70,75,
			70,75,60,
			70,71,60,
			71,60,61,
			71,61,42,
			74,74,74,//52,53,74,//37
			74,74,74,//53,72,74,
			74,74,74,//72,74,54,
			74,74,74,//54,74,55,
			74,74,74,//74,56,55,
			74,74,74,//74,73,56,
			74,74,74,//57,74,73,
			74,74,74,//52,74,57,
			77,77,77,//58,59,77,//45
			77,77,77,//59,75,77,
			77,77,77,//75,60,77,
			77,77,77,//60,61,77,
			77,77,77,//61,62,77,
			77,77,77,//62,76,77,
			77,77,77,//76,63,77,
			77,77,77,//63,58,77,
			56,55,80,//53
			55,78,80,
			78,43,44,
			43,79,44,
			79,58,81,
			58,63,81,
			78,44,80,
			44,79,81,
			80,44,45,//61
			45,44,81,
			82,80,47,//63
			47,80,46,
			80,45,46,
			45,46,81,
			46,81,51,
			81,51,83,
			47,46,48,//69
			48,46,49,
			49,46,50,
			50,46,51,
			82,84,85,//73
			82,85,47,
			47,85,86,
			47,86,48,
			48,86,49,
			49,86,87,
			49,87,88,//79
			50,49,88,
			51,50,88,
			51,88,89,
			83,51,89,
			83,89,90,
			85,84,96,//85
			85,96,97,
			86,85,97,
			86,97,98,
			86,98,87,
			87,98,88,//90
			88,98,99,
			88,99,89,
			89,99,100,
			89,100,90,
			84,95,96,//95
			96,95,103,
			103,95,94,
			103,94,102,
			102,94,93,
			102,93,92,//100
			102,92,101,
			101,92,91,
			101,91,100,
			100,91,90,
			33,0,52,//105
			0,1,52,
			1,2,52,
			52,2,57,
			2,3,57,
			3,4,57,
			57,4,73,
			4,5,73,
			73,5,56,
			5,6,56,
			6,80,56,//56,6,80,//115
			6,80,82,//6,7,80,
			6,82,84,//7,8,80,
			6,7,84,//80,8,82,
			7,8,84,//8,9,82,
			8,9,84,//82,9,84,
			9,10,84,
			84,10,95,//122
			10,11,95,
			95,11,94,
			11,12,94,
			12,13,94,
			13,14,94,
			94,14,93,
			93,14,15,
			93,15,16,
			93,16,17,//131
			93,17,18,
			93,18,92,
			92,18,19,
			92,19,20,
			92,20,21,
			92,21,91,
			91,21,22,
			90,91,22,
			90,22,23,//140
			90,23,24,//83,90,23,
			90,24,25,//83,23,24,
			90,25,26,//81,83,24,
			83,90,26,//81,24,25,
			83,81,26,//81,25,26,
			81,26,63,//63,81,26,
			63,26,27,//147
			63,27,28,
			63,28,76,
			76,28,29,
			76,29,62,
			62,29,30,
			62,30,61,
			61,30,31,
			61,31,32,
			61,32,42
	};

	private int[] standardPoints = new int[] {//106*2=212
			425,287,//0
			424,308,//1
			424,328,//2
			426,347,//3
			429,367,//4
			432,386,//5
			436,406,//6
			440,426,//7
			446,445,//8
			454,463,//9
			464,481,//10
			476,497,//11
			489,511,//12
			505,525,//13
			522,537,//14
			543,545,//15
			567,547,//16
			591,545,//17
			612,537,//18
			630,525,//19
			645,511,//20
			659,497,//21
			671,481,//22
			681,463,//23
			689,445,//24
			695,425,//25
			699,405,//26
			702,385,//27
			705,365,//28
			707,346,//29
			709,326,//30
			709,305,//31
			708,284,//32
			449,274,//33
			465,254,//34
			489,248,//35
			515,255,//36
			535,267,//37
			595,269,//38
			616,259,//39
			640,253,//40
			665,256,//41
			684,271,//42
			566,308,//43
			566,338,//44
			566,369,//45
			566,399,//46
			537,410,//47
			551,415,//48
			566,421,//49
			582,415,//50
			595,410,//51
			471,308,//52
			483,299,//53
			515,303,//54
			527,316,//55
			512,318,//56
			482,316,//57
			604,314,//58
			617,301,//59
			650,297,//60
			663,304,//61
			652,313,//62
			620,317,//63
			468,270,//64
			488,269,//65
			510,272,//66
			531,280,//67
			598,281,//68
			619,274,//69
			641,271,//70
			662,271,//71
			499,296,//72
			496,320,//73
			499,307,//74
			634,295,//75
			637,318,//76
			634,305,//77
			546,317,//78
			587,317,//79
			535,376,//80
			598,376,//81
			526,400,//82
			607,400,//83
			513,456,//84
			533,451,//85
			555,448,//86
			568,451,//87
			580,448,//88
			598,451,//89
			615,456,//90
			602,466,//91
			587,474,//92
			567,477,//93
			545,475,//94
			528,467,//95
			521,457,//96
			541,457,//97
			567,459,//98
			591,457,//99
			608,457,//100
			592,460,//101
			567,463,//102
			540,460,//103
			499,307,//104
			634,305,//105

	};

	private PointF[] standardPointFs = new PointF[] {//106*2=212
			new PointF(425,287),//0
			new PointF(424,308),//1
			new PointF(424,328),//2
            new PointF(426,347),//3
            new PointF(429,367),//4
            new PointF(432,386),//5
            new PointF(436,406),//6
            new PointF(440,426),//7
            new PointF(446,445),//8
            new PointF(454,463),//9
            new PointF(464,481),//10
            new PointF(476,497),//11
            new PointF(489,511),//12
            new PointF(505,525),//13
            new PointF(522,537),//14
            new PointF(543,545),//15
            new PointF(567,547),//16
            new PointF(591,545),//17
            new PointF(612,537),//18
            new PointF(630,525),//19
            new PointF(645,511),//20
            new PointF(659,497),//21
            new PointF(671,481),//22
            new PointF(681,463),//23
            new PointF(689,445),//24
            new PointF(695,425),//25
            new PointF(699,405),//26
            new PointF(702,385),//27
            new PointF(705,365),//28
            new PointF(707,346),//29
            new PointF(709,326),//30
            new PointF(709,305),//31
            new PointF(708,284),//32
            new PointF(449,274),//33
            new PointF(465,254),//34
            new PointF(489,248),//35
            new PointF(515,255),//36
            new PointF(535,267),//37
            new PointF(595,269),//38
            new PointF(616,259),//39
            new PointF(640,253),//40
            new PointF(665,256),//41
            new PointF(684,271),//42
            new PointF(566,308),//43
            new PointF(566,338),//44
            new PointF(566,369),//45
            new PointF(566,399),//46
            new PointF(537,410),//47
            new PointF(551,415),//48
            new PointF(566,421),//49
            new PointF(582,415),//50
            new PointF(595,410),//51
            new PointF(471,308),//52
            new PointF(483,299),//53
            new PointF(515,303),//54
            new PointF(527,316),//55
            new PointF(512,318),//56
            new PointF(482,316),//57
            new PointF(604,314),//58
            new PointF(617,301),//59
            new PointF(650,297),//60
            new PointF(663,304),//61
            new PointF(652,313),//62
            new PointF(620,317),//63
            new PointF(468,270),//64
            new PointF(488,269),//65
            new PointF(510,272),//66
            new PointF(531,280),//67
            new PointF(598,281),//68
            new PointF(619,274),//69
            new PointF(641,271),//70
            new PointF(662,271),//71
            new PointF(499,296),//72
            new PointF(496,320),//73
            new PointF(499,307),//74
            new PointF(634,295),//75
            new PointF(637,318),//76
            new PointF(634,305),//77
            new PointF(546,317),//78
            new PointF(587,317),//79
            new PointF(535,376),//80
            new PointF(598,376),//81
            new PointF(526,400),//82
            new PointF(607,400),//83
            new PointF(513,456),//84
            new PointF(533,451),//85
            new PointF(555,448),//86
            new PointF(568,451),//87
            new PointF(580,448),//88
            new PointF(598,451),//89
            new PointF(615,456),//90
            new PointF(602,466),//91
            new PointF(587,474),//92
            new PointF(567,477),//93
            new PointF(545,475),//94
            new PointF(528,467),//95
            new PointF(521,457),//96
            new PointF(541,457),//97
            new PointF(567,459),//98
            new PointF(591,457),//99
            new PointF(608,457),//100
            new PointF(592,460),//101
            new PointF(567,463),//102
            new PointF(540,460),//103
            new PointF(499,307),//104
            new PointF(634,305)//105
	};




	// vertex着色器code
	private final String vertexShaderCode = ""
			+ "attribute vec4 a_pos;"
			+ "attribute vec2 a_tc;"
			+ "varying vec2 v_tc;"
			+ "void main()"
			+ "{"
			+ "    gl_Position = a_pos;"
			+ "    v_tc = a_tc;"
			+ "}";

	// fragment着色器code
	private final String fragmentShaderCode = ""
			+ "precision mediump float;"
			+ "varying vec2 v_tc;\n"
			+ "uniform sampler2D u_texture0;\n"//face
//			+ "uniform sampler2D u_texture1;\n"//video

//			+ "uniform float u_lVideoMean;\n"
//			+ "uniform float u_aVideoMean;\n"
//			+ "uniform float u_bVideoMean;\n"
//
//			+ "uniform float u_lVideoStdDev;\n"
//			+ "uniform float u_aVideoStdDev;\n"
//			+ "uniform float u_bVideoStdDev;\n"
//
//			+ "uniform float u_lUserMean;\n"
//			+ "uniform float u_aUserMean;\n"
//			+ "uniform float u_bUserMean;\n"
//
//			+ "uniform float u_lUserStdDev;\n"
//			+ "uniform float u_aUserStdDev;\n"
//			+ "uniform float u_bUserStdDev;\n"

			+ "void main() {"
			+ "    vec4 color0 = texture2D(u_texture0, v_tc ).bgra;\n"
//            + "    vec4 color1 = texture2D(u_texture1, v_tc );\n"
//            + "    vec4 finalColor = mix(color1, color0, color0.a);"
			+ "    color0.a *= 0.85;\n"
			+ "    gl_FragColor = color0;\n"
			+ "}";


	private int mProgram;

	private int mTexture0ID = -1;


	private int mWidthUsers = 1280;
	private int mHeightUsers = 720;
	private int mWidthStars = 1280;
	private int mHeightStars = 720;

	SkinInfo mSkinUsersInfo;
//	boolean mNeedUsersSamples;
	private void openPicture(Context ctx) {
		InputStream is = ctx.getResources().openRawResource(R.raw.gagaga005);
		Bitmap bitmap = BitmapFactory.decodeStream(is);

//		if (mNeedUsersSamples) {
		mSkinUsersInfo = getSkinInfo(bitmap, standardPointFs);
		float[] labMean = mSkinUsersInfo.getLabMean();
		float[] labStdDev = mSkinUsersInfo.getLabStandardDeviation();
		Log.e("shiyang", "shiyang lab mean user =("+labMean[0]+","+labMean[1]+","+labMean[2]+")");
		Log.e("shiyang", "shiyang lab stddev user =("+labStdDev[0]+","+labStdDev[1]+","+labStdDev[2]+")");

//			mNeedUsersSamples = false;
//		}

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
		Bitmap.Config cfg =  bitmap.getConfig();
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
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, w,h,0,GLES20.GL_RGBA,GLES20.GL_UNSIGNED_BYTE, pixels);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		// Recycle the bitmap, since its data has been loaded into OpenGL.
		bitmap.recycle();
		this.mTexture0ID =  tex[0];
		this.mWidthUsers = w;
		this.mHeightUsers = h;

	}

	private FloatBuffer vtxBuffer, texBuffer;
	private ShortBuffer idxBuffer;

	private int[] m_vtxBufId = new int[1];
	private int[] m_texBufId = new int[1];
	private int[] m_idxBufId = new int[1];

	private float mAlpha = -0.15f;
	private float mmiixx(float v0, float v1, float alpha) {
	    return (1.0f-alpha) * v0 + alpha * v1;
	}


	private void initMesh() {
		Log.e("shiyang", "shiyang initMesh");
		float[] t_v = new float[106*2];
        for (int i=0; i<106; i++) {
        	int idx = 2*i;
			t_v[idx+0] = standardPoints[idx+0]*1.0f/this.mWidthUsers*2.0f-1.0f;
			t_v[idx+1] = standardPoints[idx+1]*1.0f/this.mHeightUsers*2.0f-1.0f;

//			t_v[idx+0] *= -1.0;
			t_v[idx+1] *= -1.0;
		}

        for (int i=0; i<5; i++) { //5 in all
            int idx0 = 2*(i+33); //33～37
            int idx1 = 2*(i+52);
            t_v[idx0+0] = mmiixx(t_v[idx0+0], t_v[idx1+0], mAlpha);
            t_v[idx0+1] = mmiixx(t_v[idx0+1], t_v[idx1+1], mAlpha);
			int idx2 = 2*(i+38); //38~42
			int idx3 = 2*(i+58);
			t_v[idx2+0] = mmiixx(t_v[idx2+0], t_v[idx3+0], mAlpha);
			t_v[idx2+1] = mmiixx(t_v[idx2+1], t_v[idx3+1], mAlpha);
        }

		ByteBuffer bb = ByteBuffer.allocateDirect(t_v.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vtxBuffer = bb.asFloatBuffer();
		vtxBuffer.put(t_v);
		vtxBuffer.position(0);

		GLES20.glGenBuffers(1, m_vtxBufId, 0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, m_vtxBufId[0]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,vtxBuffer.capacity() * 4, vtxBuffer, GLES20.GL_DYNAMIC_DRAW);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

		float[] t_t = new float[106*2];
		for (int i=0; i<106; i++) {
			int idx = 2*i;
			t_t[idx+0] = standardPoints[idx+0]*1.0f/this.mWidthUsers;
			t_t[idx+1] = standardPoints[idx+1]*1.0f/this.mHeightUsers;
		}

		for (int i=0; i<5; i++) { //5 in all
			int idx0 = 2*(i+33); //33～37
			int idx1 = 2*(i+52);
			t_t[idx0+0] = mmiixx(t_t[idx0+0], t_t[idx1+0], mAlpha);
			t_t[idx0+1] = mmiixx(t_t[idx0+1], t_t[idx1+1], mAlpha);
			int idx2 = 2*(i+38); //38~42
			int idx3 = 2*(i+58);
			t_t[idx2+0] = mmiixx(t_t[idx2+0], t_t[idx3+0], mAlpha);
			t_t[idx2+1] = mmiixx(t_t[idx2+1], t_t[idx3+1], mAlpha);
		}

		ByteBuffer cc = ByteBuffer.allocateDirect(t_t.length * 4);
		cc.order(ByteOrder.nativeOrder());
		texBuffer = cc.asFloatBuffer();
		texBuffer.put(t_t);
		texBuffer.position(0);

		GLES20.glGenBuffers(1, m_texBufId, 0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, m_texBufId[0]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,texBuffer.capacity() * 4, texBuffer, GLES20.GL_DYNAMIC_DRAW);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

		short[] t_i = new short[157*3];
		for (int i=0; i<157; i++) {
			int idx = 3*i;
			t_i[idx+0] = indexArray[idx+0];
			t_i[idx+1] = indexArray[idx+1];
			t_i[idx+2] = indexArray[idx+2];
		}

		ByteBuffer dd = ByteBuffer.allocateDirect(t_i.length * 2);
		dd.order(ByteOrder.nativeOrder());
		idxBuffer = dd.asShortBuffer();
		idxBuffer.put(t_i);
		idxBuffer.position(0);

		GLES20.glGenBuffers(1, m_idxBufId, 0);
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, m_idxBufId[0]);
		GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER,idxBuffer.capacity() * 2, idxBuffer, GLES20.GL_STATIC_DRAW);
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public void setTrackerData(PointF[] data) {
		Log.e("shiyang", "shiyang setTrackerData");
		for (int i=0; i<106; i++) {
			int idx = 2*i;
			standardPoints[idx+0] = (int) data[i].x;
			standardPoints[idx+1] = (int) data[i].y;
		}




//		float[] t_v = new float[106*2];
//		for (int i=0; i<106; i++) {
//			int idx = 2*i;
//			t_v[idx+0] = data[i].x*1.0f/1080.0f*2.0f-1.0f;
//			t_v[idx+1] = data[i].y*1.0f/720.0f*2.0f-1.0f;
//			t_v[idx+0] = 0.0f;
//			t_v[idx+1] = 0.0f;
//
////			t_v[idx+0] *= -1.0;
//			t_v[idx+1] *= -1.0;
//		}
//
//		ByteBuffer bb = ByteBuffer.allocateDirect(t_v.length * 4);
//		bb.order(ByteOrder.nativeOrder());
//		vtxBuffer = bb.asFloatBuffer();
//		vtxBuffer.put(t_v);
//		vtxBuffer.position(0);
//
////		GLES20.glGenBuffers(1, m_vtxBufId, 0);
//		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, m_vtxBufId[0]);
//		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,vtxBuffer.capacity() * 4, vtxBuffer, GLES20.GL_DYNAMIC_DRAW);
////		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
//
//		float[] t_t = new float[106*2];
//		for (int i=0; i<106; i++) {
//			int idx = 2*i;
//			t_t[idx+0] = data[i].x*1.0f/1080.0f;
//			t_t[idx+1] = data[i].y*1.0f/720.0f;
//		}
//
//		ByteBuffer cc = ByteBuffer.allocateDirect(t_t.length * 4);
//		cc.order(ByteOrder.nativeOrder());
//		texBuffer = cc.asFloatBuffer();
//		texBuffer.put(t_t);
//		texBuffer.position(0);
//
////		GLES20.glGenBuffers(1, m_texBufId, 0);
//		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, m_texBufId[0]);
//		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,texBuffer.capacity() * 4, texBuffer, GLES20.GL_DYNAMIC_DRAW);
////		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
	}

	public SwapMatrix(Context ctx) {
			openPicture(ctx);
            initMesh();

		mProgram = GLES20.glCreateProgram(); // create empty OpenGL ES Program

		// 拿出两个着色器 顶点着色器和碎片着色器
		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		GLES20.glAttachShader(mProgram, vertexShader); // add the vertex shader

		int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
				fragmentShaderCode);
		GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment
		// shader to program
		GLES20.glLinkProgram(mProgram); // creates OpenGL ES program executables




	}

//	private void doUpdate() {
//		int w = 640;
//		int h = 480;
//		doUpdate(w, h);
//	}

	private void doUpdate(int w, int h) {
		float[] t_v = new float[106*2];
		for (int i=0; i<106; i++) {
			int idx = 2*i;
			t_v[idx+0] = standardPoints[idx+0]*1.0f/w*2.0f-1.0f;
			t_v[idx+1] = standardPoints[idx+1]*1.0f/h*2.0f-1.0f;

//			t_v[idx+0] *= -1.0;
			t_v[idx+1] *= -1.0;
		}

		for (int i=0; i<5; i++) { //5 in all
			int idx0 = 2*(i+33); //33～37
			int idx1 = 2*(i+52);
			t_v[idx0+0] = mmiixx(t_v[idx0+0], t_v[idx1+0], mAlpha);
			t_v[idx0+1] = mmiixx(t_v[idx0+1], t_v[idx1+1], mAlpha);
			int idx2 = 2*(i+38); //38~42
			int idx3 = 2*(i+58);
			t_v[idx2+0] = mmiixx(t_v[idx2+0], t_v[idx3+0], mAlpha);
			t_v[idx2+1] = mmiixx(t_v[idx2+1], t_v[idx3+1], mAlpha);
		}

		ByteBuffer bb = ByteBuffer.allocateDirect(t_v.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vtxBuffer = bb.asFloatBuffer();
		vtxBuffer.put(t_v);
		vtxBuffer.position(0);

//		GLES20.glGenBuffers(1, m_vtxBufId, 0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, m_vtxBufId[0]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,vtxBuffer.capacity() * 4, vtxBuffer, GLES20.GL_DYNAMIC_DRAW);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

//		float[] t_t = new float[106*2];
//		for (int i=0; i<106; i++) {
//			int idx = 2*i;
//			t_t[idx+1] = standardPoints[idx+0]*1.0f/w;
//			t_t[idx+0] = standardPoints[idx+1]*1.0f/h;
//		}
//
//		ByteBuffer cc = ByteBuffer.allocateDirect(t_t.length * 4);
//		cc.order(ByteOrder.nativeOrder());
//		texBuffer = cc.asFloatBuffer();
//		texBuffer.put(t_t);
//		texBuffer.position(0);
//
////		GLES20.glGenBuffers(1, m_texBufId, 0);
//		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, m_texBufId[0]);
//		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,texBuffer.capacity() * 4, texBuffer, GLES20.GL_DYNAMIC_DRAW);
////		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
	}

	/**
	 * 绘制：
	 *
	 * 我们在 onDrawFrame 回调中执行绘制操作，绘制的过程其实就是为 shader 代码变量赋值，并调用绘制命令的过程：
	 */
	public void draw(float[] mtx) {
		draw(mtx, 0, 640, 480);
	}


	public void draw(float[] mtx, int id1, int w, int h) {

		int e = -9999;



		GLES20.glUseProgram(mProgram);

		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture0ID);
		int loc = GLES20.glGetUniformLocation(mProgram, "u_texture0");
		GLES20.glUniform1i(loc, 0);

        doUpdate(w, h);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, m_vtxBufId[0]);
		int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_pos");
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(mPositionHandle, 2, GLES20.GL_FLOAT, false, 0, 0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, m_texBufId[0]);
		int mTextureCoordHandle = GLES20.glGetAttribLocation(mProgram, "a_tc");
		GLES20.glEnableVertexAttribArray(mTextureCoordHandle);
		GLES20.glVertexAttribPointer(mTextureCoordHandle, 2, GLES20.GL_FLOAT, false, 0, 0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		e=GLES20.glGetError();
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, m_idxBufId[0]);
        e=GLES20.glGetError();
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexArray.length, GLES20.GL_UNSIGNED_SHORT, 0);

//		Log.e("shiyang" , "shiyang idxbuf cap = "+idxBuffer.capacity());
//		Log.e("shiyang" , "shiyang idxAry len = "+indexArray.length);

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

	public SkinInfo getSwapSkinInfo() {
		return mSkinUsersInfo;
	}

}



//private void lalala()
//{
//	{"time_used": 436, "image_id": "/uFHXAIk3dCyPrVW4EWH/w==",
//			"faces": [{"landmark": {
//		"contour_chin": {"y": 547, "x": 567},//16
//		"left_eye_upper_left_quarter": {"y": 299, "x": 483},//53
//		"mouth_lower_lip_right_contour1": {"y": 460, "x": 592},//101
//		"left_eye_bottom": {"y": 320, "x": 496},//73
//		"mouth_lower_lip_right_contour2": {"y": 466, "x": 602},//91
//		"contour_left7": {"y": 406, "x": 436},//6
//		"contour_left6": {"y": 386, "x": 432},//5
//		"contour_left5": {"y": 367, "x": 429},//4
//		"contour_left4": {"y": 347, "x": 426},//3
//		"contour_left3": {"y": 328, "x": 424},//2
//		"contour_left2": {"y": 308, "x": 424},//1
//		"contour_left1": {"y": 287, "x": 425},//0
//		"left_eye_lower_left_quarter": {"y": 316, "x": 482},//57
//		"contour_right1": {"y": 284, "x": 708},//32
//		"contour_right3": {"y": 326, "x": 709},//30
//		"contour_right2": {"y": 305, "x": 709},//31
//		"contour_right5": {"y": 365, "x": 705},//28
//		"contour_right4": {"y": 346, "x": 707},//29
//		"contour_left9": {"y": 445, "x": 446},//8
//		"contour_right6": {"y": 385, "x": 702},//27
//		"right_eye_right_corner": {"y": 304, "x": 663},//61
//		"nose_bridge1": {"y": 308, "x": 566},//43
//		"nose_bridge3": {"y": 369, "x": 566},//45
//		"nose_bridge2": {"y": 338, "x": 566},//44
//		"right_eyebrow_upper_left_corner": {"y": 269, "x": 595},//38
//		"nose_right_contour4": {"y": 410, "x": 595},//51
//		"nose_right_contour1": {"y": 317, "x": 587},//79
//		"right_eye_left_corner": {"y": 314, "x": 604},//58
//		"left_eyebrow_upper_right_corner": {"y": 267, "x": 535},//37
//		"left_eyebrow_upper_middle": {"y": 248, "x": 489},//35
//		"mouth_lower_lip_right_contour3": {"y": 474, "x": 587},//92
//		"nose_left_contour3": {"y": 400, "x": 526},//82
//		"mouth_lower_lip_bottom": {"y": 477, "x": 567},//93
//		"nose_right_contour2": {"y": 376, "x": 598},//81
//		"left_eye_top": {"y": 296, "x": 499},//72
//		"nose_left_contour1": {"y": 317, "x": 546},//78
//		"mouth_upper_lip_bottom": {"y": 459, "x": 567},//98
//		"mouth_upper_lip_left_contour2": {"y": 451, "x": 533},//85
//		"mouth_upper_lip_top": {"y": 451, "x": 568},//87
//		"mouth_upper_lip_left_contour1": {"y": 448, "x": 555},//86
//		"mouth_upper_lip_left_contour4": {"y": 457, "x": 541},//97
//		"right_eye_top": {"y": 295, "x": 634},//75
//		"right_eye_bottom": {"y": 318, "x": 637},//76
//		"right_eyebrow_lower_left_corner": {"y": 281, "x": 598},//68
//		"mouth_left_corner": {"y": 456, "x": 513},//84
//		"nose_middle_contour": {"y": 421, "x": 566},//49
//		"right_eye_lower_right_quarter": {"y": 313, "x": 652},//62
//		"right_eyebrow_lower_right_quarter": {"y": 271, "x": 662},//71
//		"contour_right9": {"y": 445, "x": 689},//24
//		"mouth_right_corner": {"y": 456, "x": 615},//90
//		"right_eye_lower_left_quarter": {"y": 317, "x": 620},//63
//		"right_eye_center": {"y": 305, "x": 634},//77(77 or 105)
//		"contour_right13": {"y": 511, "x": 645},//20
//		"right_eyebrow_lower_left_quarter": {"y": 274, "x": 619},//69
//		"left_eye_pupil": {"y": 307, "x": 499},//104(74 or 104)
//		"contour_right8": {"y": 425, "x": 695},//25
//		"contour_left13": {"y": 511, "x": 489},//12
//		"left_eyebrow_lower_right_quarter": {"y": 272, "x": 510},//66
//		"left_eye_right_corner": {"y": 316, "x": 527},//55
//		"left_eyebrow_lower_right_corner": {"y": 280, "x": 531},//67
//		"mouth_upper_lip_left_contour3": {"y": 457, "x": 521},//96
//		"left_eyebrow_lower_left_quarter": {"y": 270, "x": 468},//64
//		"mouth_lower_lip_left_contour1": {"y": 460, "x": 540},//103
//		"mouth_lower_lip_left_contour3": {"y": 475, "x": 545},//94
//		"mouth_lower_lip_left_contour2": {"y": 467, "x": 528},//95
//		"contour_right7": {"y": 405, "x": 699},//26
//		"left_eyebrow_left_corner": {"y": 274, "x": 449},//33
//		"nose_tip": {"y": 399, "x": 566},//46
//		"right_eyebrow_upper_middle": {"y": 253, "x": 640},//40
//		"contour_left8": {"y": 426, "x": 440},//7
//		"right_eyebrow_lower_middle": {"y": 271, "x": 641},//70
//		"left_eye_center": {"y": 307, "x": 499},//74
//		"right_eyebrow_upper_left_quarter": {"y": 259, "x": 616},//39
//		"right_eyebrow_right_corner": {"y": 271, "x": 684},//42
//		"right_eyebrow_upper_right_quarter": {"y": 256, "x": 665},//41
//		"contour_left16": {"y": 545, "x": 543},//15
//		"contour_left15": {"y": 537, "x": 522},//14
//		"contour_left14": {"y": 525, "x": 505},//13
//		"left_eyebrow_upper_right_quarter": {"y": 255, "x": 515},//36
//		"contour_left12": {"y": 497, "x": 476},//11
//		"contour_left11": {"y": 481, "x": 464},//10
//		"contour_left10": {"y": 463, "x": 454},//9
//		"left_eyebrow_lower_middle": {"y": 269, "x": 488},//65
//		"left_eyebrow_upper_left_quarter": {"y": 254, "x": 465},//34
//		"right_eye_upper_right_quarter": {"y": 297, "x": 650},//60
//		"nose_right_contour3": {"y": 400, "x": 607},//83
//		"mouth_upper_lip_right_contour4": {"y": 457, "x": 591},//99
//		"nose_right_contour5": {"y": 415, "x": 582},//50
//		"nose_left_contour4": {"y": 410, "x": 537},//47
//		"nose_left_contour5": {"y": 415, "x": 551},//48
//		"nose_left_contour2": {"y": 376, "x": 535},//80
//		"mouth_upper_lip_right_contour1": {"y": 448, "x": 580},//88
//		"mouth_upper_lip_right_contour2": {"y": 451, "x": 598},//89
//		"mouth_upper_lip_right_contour3": {"y": 457, "x": 608},//100
//		"left_eye_left_corner": {"y": 308, "x": 471},//52
//		"contour_right15": {"y": 537, "x": 612},//18
//		"contour_right14": {"y": 525, "x": 630},//19
//		"contour_right16": {"y": 545, "x": 591},//17
//		"contour_right11": {"y": 481, "x": 671},//22
//		"contour_right10": {"y": 463, "x": 681},//23
//		"left_eye_upper_right_quarter": {"y": 303, "x": 515},//54
//		"contour_right12": {"y": 497, "x": 659},//21
//		"left_eye_lower_right_quarter": {"y": 318, "x": 512},//56
//		"mouth_lower_lip_top": {"y": 463, "x": 567},//102
//		"right_eye_upper_left_quarter": {"y": 301, "x": 617},//59
//		"right_eye_pupil": {"y": 305, "x": 634}},//105
//
//		"attributes": {"headpose": {"yaw_angle": 4.054522,
//		"pitch_angle": 5.4693522, "roll_angle": -0.0750022}},
//		"face_token": "3747333c5642715554c5bcd057d43082",
//		"face_rectangle": {"width": 301, "top": 246, "height": 301, "left": 417}}],
//		"request_id": "1552388457,e745b36c-2d6b-4335-841c-83ba1e576888"}
//}