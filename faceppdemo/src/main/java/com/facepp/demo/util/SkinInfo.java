package com.facepp.demo.util;

public class SkinInfo {
    private float[] mLab;
	private float[] mLabMean;
	private float[] mLabStandardDeviation;

	public SkinInfo() {
		mLab = new float[] {0f,0f,0f};
		mLabMean = new float[] {0f,0f,0f};
		mLabStandardDeviation = new float[] {0f,0f,0f};
	}

	public void setLab(float[] lab) {
		setLab(lab[0], lab[1], lab[2]);
	}

	public void setLab(float l, float a, float b) {
		mLab[0] = l;
		mLab[1] = a;
		mLab[2] = b;
	}

	public void setLabMean(float[] labMean) {
		setLabMean(labMean[0], labMean[1], labMean[2]);
	}

	public void setLabMean(float lMean, float aMean, float bMean) {
		mLabMean[0] = lMean;
		mLabMean[1] = aMean;
		mLabMean[2] = bMean;
	}

	public float[] getLabMean() {
	    return mLabMean;
	}

	public void setLabStandardDeviation(float[] labStandardDeviation) {
		setLabStandardDeviation(labStandardDeviation[0], labStandardDeviation[1], labStandardDeviation[2]);
	}

	public void setLabStandardDeviation(float lStandardDeviation, float aStandardDeviation, float bStandardDeviation) {
		mLabStandardDeviation[0] = lStandardDeviation;
		mLabStandardDeviation[1] = aStandardDeviation;
		mLabStandardDeviation[2] = bStandardDeviation;
	}

	public float[] getLabStandardDeviation() {
		return mLabStandardDeviation;
	}

}

// cpu+mb   2399+2299   r7 2700x + x470 aorus
// cpu+mb   4599+5499   tr 2920x + x399 aorus
// gpu      5699   radeon vii
// ddr      749*2  ddr4 3200
// ssd      3999   4tb qlc
// = (24+23) or (46+55)+57+15+40= 47 or 101 + 112 = 159 or 213
