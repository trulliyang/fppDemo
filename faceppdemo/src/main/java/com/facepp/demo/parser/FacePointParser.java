package com.facepp.demo.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 解析 face++ 识别图片关键点的json数据, 保存到 points 数组中
 *
 * Created by mrsimple on 14/3/2019.
 */
public class FacePointParser {

    int[] standardPoints = new int[] {//106*2=212
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

    static String rawData = "{\n" +
            "  \"time_used\": 436,\n" +
            "  \"image_id\": \"/uFHXAIk3dCyPrVW4EWH/w==\",\n" +
            "  \"faces\": [\n" +
            "    {\n" +
            "      \"landmark\": {\n" +
            "        \"contour_chin\": {\n" +
            "          \"y\": 547,\n" +
            "          \"x\": 567\n" +
            "        },\n" +
            "        \"left_eye_upper_left_quarter\": {\n" +
            "          \"y\": 299,\n" +
            "          \"x\": 483\n" +
            "        },\n" +
            "        \"mouth_lower_lip_right_contour1\": {\n" +
            "          \"y\": 460,\n" +
            "          \"x\": 592\n" +
            "        },\n" +
            "        \"left_eye_bottom\": {\n" +
            "          \"y\": 320,\n" +
            "          \"x\": 496\n" +
            "        },\n" +
            "        \"mouth_lower_lip_right_contour2\": {\n" +
            "          \"y\": 466,\n" +
            "          \"x\": 602\n" +
            "        },\n" +
            "        \"contour_left7\": {\n" +
            "          \"y\": 406,\n" +
            "          \"x\": 436\n" +
            "        },\n" +
            "        \"contour_left6\": {\n" +
            "          \"y\": 386,\n" +
            "          \"x\": 432\n" +
            "        },\n" +
            "        \"contour_left5\": {\n" +
            "          \"y\": 367,\n" +
            "          \"x\": 429\n" +
            "        },\n" +
            "        \"contour_left4\": {\n" +
            "          \"y\": 347,\n" +
            "          \"x\": 426\n" +
            "        },\n" +
            "        \"contour_left3\": {\n" +
            "          \"y\": 328,\n" +
            "          \"x\": 424\n" +
            "        },\n" +
            "        \"contour_left2\": {\n" +
            "          \"y\": 308,\n" +
            "          \"x\": 424\n" +
            "        },\n" +
            "        \"contour_left1\": {\n" +
            "          \"y\": 287,\n" +
            "          \"x\": 425\n" +
            "        },\n" +
            "        \"left_eye_lower_left_quarter\": {\n" +
            "          \"y\": 316,\n" +
            "          \"x\": 482\n" +
            "        },\n" +
            "        \"contour_right1\": {\n" +
            "          \"y\": 284,\n" +
            "          \"x\": 708\n" +
            "        },\n" +
            "        \"contour_right3\": {\n" +
            "          \"y\": 326,\n" +
            "          \"x\": 709\n" +
            "        },\n" +
            "        \"contour_right2\": {\n" +
            "          \"y\": 305,\n" +
            "          \"x\": 709\n" +
            "        },\n" +
            "        \"contour_right5\": {\n" +
            "          \"y\": 365,\n" +
            "          \"x\": 705\n" +
            "        },\n" +
            "        \"contour_right4\": {\n" +
            "          \"y\": 346,\n" +
            "          \"x\": 707\n" +
            "        },\n" +
            "        \"contour_left9\": {\n" +
            "          \"y\": 445,\n" +
            "          \"x\": 446\n" +
            "        },\n" +
            "        \"contour_right6\": {\n" +
            "          \"y\": 385,\n" +
            "          \"x\": 702\n" +
            "        },\n" +
            "        \"right_eye_right_corner\": {\n" +
            "          \"y\": 304,\n" +
            "          \"x\": 663\n" +
            "        },\n" +
            "        \"nose_bridge1\": {\n" +
            "          \"y\": 308,\n" +
            "          \"x\": 566\n" +
            "        },\n" +
            "        \"nose_bridge3\": {\n" +
            "          \"y\": 369,\n" +
            "          \"x\": 566\n" +
            "        },\n" +
            "        \"nose_bridge2\": {\n" +
            "          \"y\": 338,\n" +
            "          \"x\": 566\n" +
            "        },\n" +
            "        \"right_eyebrow_upper_left_corner\": {\n" +
            "          \"y\": 269,\n" +
            "          \"x\": 595\n" +
            "        },\n" +
            "        \"nose_right_contour4\": {\n" +
            "          \"y\": 410,\n" +
            "          \"x\": 595\n" +
            "        },\n" +
            "        \"nose_right_contour1\": {\n" +
            "          \"y\": 317,\n" +
            "          \"x\": 587\n" +
            "        },\n" +
            "        \"right_eye_left_corner\": {\n" +
            "          \"y\": 314,\n" +
            "          \"x\": 604\n" +
            "        },\n" +
            "        \"left_eyebrow_upper_right_corner\": {\n" +
            "          \"y\": 267,\n" +
            "          \"x\": 535\n" +
            "        },\n" +
            "        \"left_eyebrow_upper_middle\": {\n" +
            "          \"y\": 248,\n" +
            "          \"x\": 489\n" +
            "        },\n" +
            "        \"mouth_lower_lip_right_contour3\": {\n" +
            "          \"y\": 474,\n" +
            "          \"x\": 587\n" +
            "        },\n" +
            "        \"nose_left_contour3\": {\n" +
            "          \"y\": 400,\n" +
            "          \"x\": 526\n" +
            "        },\n" +
            "        \"mouth_lower_lip_bottom\": {\n" +
            "          \"y\": 477,\n" +
            "          \"x\": 567\n" +
            "        },\n" +
            "        \"nose_right_contour2\": {\n" +
            "          \"y\": 376,\n" +
            "          \"x\": 598\n" +
            "        },\n" +
            "        \"left_eye_top\": {\n" +
            "          \"y\": 296,\n" +
            "          \"x\": 499\n" +
            "        },\n" +
            "        \"nose_left_contour1\": {\n" +
            "          \"y\": 317,\n" +
            "          \"x\": 546\n" +
            "        },\n" +
            "        \"mouth_upper_lip_bottom\": {\n" +
            "          \"y\": 459,\n" +
            "          \"x\": 567\n" +
            "        },\n" +
            "        \"mouth_upper_lip_left_contour2\": {\n" +
            "          \"y\": 451,\n" +
            "          \"x\": 533\n" +
            "        },\n" +
            "        \"mouth_upper_lip_top\": {\n" +
            "          \"y\": 451,\n" +
            "          \"x\": 568\n" +
            "        },\n" +
            "        \"mouth_upper_lip_left_contour1\": {\n" +
            "          \"y\": 448,\n" +
            "          \"x\": 555\n" +
            "        },\n" +
            "        \"mouth_upper_lip_left_contour4\": {\n" +
            "          \"y\": 457,\n" +
            "          \"x\": 541\n" +
            "        },\n" +
            "        \"right_eye_top\": {\n" +
            "          \"y\": 295,\n" +
            "          \"x\": 634\n" +
            "        },\n" +
            "        \"right_eye_bottom\": {\n" +
            "          \"y\": 318,\n" +
            "          \"x\": 637\n" +
            "        },\n" +
            "        \"right_eyebrow_lower_left_corner\": {\n" +
            "          \"y\": 281,\n" +
            "          \"x\": 598\n" +
            "        },\n" +
            "        \"mouth_left_corner\": {\n" +
            "          \"y\": 456,\n" +
            "          \"x\": 513\n" +
            "        },\n" +
            "        \"nose_middle_contour\": {\n" +
            "          \"y\": 421,\n" +
            "          \"x\": 566\n" +
            "        },\n" +
            "        \"right_eye_lower_right_quarter\": {\n" +
            "          \"y\": 313,\n" +
            "          \"x\": 652\n" +
            "        },\n" +
            "        \"right_eyebrow_lower_right_quarter\": {\n" +
            "          \"y\": 271,\n" +
            "          \"x\": 662\n" +
            "        },\n" +
            "        \"contour_right9\": {\n" +
            "          \"y\": 445,\n" +
            "          \"x\": 689\n" +
            "        },\n" +
            "        \"mouth_right_corner\": {\n" +
            "          \"y\": 456,\n" +
            "          \"x\": 615\n" +
            "        },\n" +
            "        \"right_eye_lower_left_quarter\": {\n" +
            "          \"y\": 317,\n" +
            "          \"x\": 620\n" +
            "        },\n" +
            "        \"right_eye_center\": {\n" +
            "          \"y\": 305,\n" +
            "          \"x\": 634\n" +
            "        },\n" +
            "        \"contour_right13\": {\n" +
            "          \"y\": 511,\n" +
            "          \"x\": 645\n" +
            "        },\n" +
            "        \"right_eyebrow_lower_left_quarter\": {\n" +
            "          \"y\": 274,\n" +
            "          \"x\": 619\n" +
            "        },\n" +
            "        \"left_eye_pupil\": {\n" +
            "          \"y\": 307,\n" +
            "          \"x\": 499\n" +
            "        },\n" +
            "        \"contour_right8\": {\n" +
            "          \"y\": 425,\n" +
            "          \"x\": 695\n" +
            "        },\n" +
            "        \"contour_left13\": {\n" +
            "          \"y\": 511,\n" +
            "          \"x\": 489\n" +
            "        },\n" +
            "        \"left_eyebrow_lower_right_quarter\": {\n" +
            "          \"y\": 272,\n" +
            "          \"x\": 510\n" +
            "        },\n" +
            "        \"left_eye_right_corner\": {\n" +
            "          \"y\": 316,\n" +
            "          \"x\": 527\n" +
            "        },\n" +
            "        \"left_eyebrow_lower_right_corner\": {\n" +
            "          \"y\": 280,\n" +
            "          \"x\": 531\n" +
            "        },\n" +
            "        \"mouth_upper_lip_left_contour3\": {\n" +
            "          \"y\": 457,\n" +
            "          \"x\": 521\n" +
            "        },\n" +
            "        \"left_eyebrow_lower_left_quarter\": {\n" +
            "          \"y\": 270,\n" +
            "          \"x\": 468\n" +
            "        },\n" +
            "        \"mouth_lower_lip_left_contour1\": {\n" +
            "          \"y\": 460,\n" +
            "          \"x\": 540\n" +
            "        },\n" +
            "        \"mouth_lower_lip_left_contour3\": {\n" +
            "          \"y\": 475,\n" +
            "          \"x\": 545\n" +
            "        },\n" +
            "        \"mouth_lower_lip_left_contour2\": {\n" +
            "          \"y\": 467,\n" +
            "          \"x\": 528\n" +
            "        },\n" +
            "        \"contour_right7\": {\n" +
            "          \"y\": 405,\n" +
            "          \"x\": 699\n" +
            "        },\n" +
            "        \"left_eyebrow_left_corner\": {\n" +
            "          \"y\": 274,\n" +
            "          \"x\": 449\n" +
            "        },\n" +
            "        \"nose_tip\": {\n" +
            "          \"y\": 399,\n" +
            "          \"x\": 566\n" +
            "        },\n" +
            "        \"right_eyebrow_upper_middle\": {\n" +
            "          \"y\": 253,\n" +
            "          \"x\": 640\n" +
            "        },\n" +
            "        \"contour_left8\": {\n" +
            "          \"y\": 426,\n" +
            "          \"x\": 440\n" +
            "        },\n" +
            "        \"right_eyebrow_lower_middle\": {\n" +
            "          \"y\": 271,\n" +
            "          \"x\": 641\n" +
            "        },\n" +
            "        \"left_eye_center\": {\n" +
            "          \"y\": 307,\n" +
            "          \"x\": 499\n" +
            "        },\n" +
            "        \"right_eyebrow_upper_left_quarter\": {\n" +
            "          \"y\": 259,\n" +
            "          \"x\": 616\n" +
            "        },\n" +
            "        \"right_eyebrow_right_corner\": {\n" +
            "          \"y\": 271,\n" +
            "          \"x\": 684\n" +
            "        },\n" +
            "        \"right_eyebrow_upper_right_quarter\": {\n" +
            "          \"y\": 256,\n" +
            "          \"x\": 665\n" +
            "        },\n" +
            "        \"contour_left16\": {\n" +
            "          \"y\": 545,\n" +
            "          \"x\": 543\n" +
            "        },\n" +
            "        \"contour_left15\": {\n" +
            "          \"y\": 537,\n" +
            "          \"x\": 522\n" +
            "        },\n" +
            "        \"contour_left14\": {\n" +
            "          \"y\": 525,\n" +
            "          \"x\": 505\n" +
            "        },\n" +
            "        \"left_eyebrow_upper_right_quarter\": {\n" +
            "          \"y\": 255,\n" +
            "          \"x\": 515\n" +
            "        },\n" +
            "        \"contour_left12\": {\n" +
            "          \"y\": 497,\n" +
            "          \"x\": 476\n" +
            "        },\n" +
            "        \"contour_left11\": {\n" +
            "          \"y\": 481,\n" +
            "          \"x\": 464\n" +
            "        },\n" +
            "        \"contour_left10\": {\n" +
            "          \"y\": 463,\n" +
            "          \"x\": 454\n" +
            "        },\n" +
            "        \"left_eyebrow_lower_middle\": {\n" +
            "          \"y\": 269,\n" +
            "          \"x\": 488\n" +
            "        },\n" +
            "        \"left_eyebrow_upper_left_quarter\": {\n" +
            "          \"y\": 254,\n" +
            "          \"x\": 465\n" +
            "        },\n" +
            "        \"right_eye_upper_right_quarter\": {\n" +
            "          \"y\": 297,\n" +
            "          \"x\": 650\n" +
            "        },\n" +
            "        \"nose_right_contour3\": {\n" +
            "          \"y\": 400,\n" +
            "          \"x\": 607\n" +
            "        },\n" +
            "        \"mouth_upper_lip_right_contour4\": {\n" +
            "          \"y\": 457,\n" +
            "          \"x\": 591\n" +
            "        },\n" +
            "        \"nose_right_contour5\": {\n" +
            "          \"y\": 415,\n" +
            "          \"x\": 582\n" +
            "        },\n" +
            "        \"nose_left_contour4\": {\n" +
            "          \"y\": 410,\n" +
            "          \"x\": 537\n" +
            "        },\n" +
            "        \"nose_left_contour5\": {\n" +
            "          \"y\": 415,\n" +
            "          \"x\": 551\n" +
            "        },\n" +
            "        \"nose_left_contour2\": {\n" +
            "          \"y\": 376,\n" +
            "          \"x\": 535\n" +
            "        },\n" +
            "        \"mouth_upper_lip_right_contour1\": {\n" +
            "          \"y\": 448,\n" +
            "          \"x\": 580\n" +
            "        },\n" +
            "        \"mouth_upper_lip_right_contour2\": {\n" +
            "          \"y\": 451,\n" +
            "          \"x\": 598\n" +
            "        },\n" +
            "        \"mouth_upper_lip_right_contour3\": {\n" +
            "          \"y\": 457,\n" +
            "          \"x\": 608\n" +
            "        },\n" +
            "        \"left_eye_left_corner\": {\n" +
            "          \"y\": 308,\n" +
            "          \"x\": 471\n" +
            "        },\n" +
            "        \"contour_right15\": {\n" +
            "          \"y\": 537,\n" +
            "          \"x\": 612\n" +
            "        },\n" +
            "        \"contour_right14\": {\n" +
            "          \"y\": 525,\n" +
            "          \"x\": 630\n" +
            "        },\n" +
            "        \"contour_right16\": {\n" +
            "          \"y\": 545,\n" +
            "          \"x\": 591\n" +
            "        },\n" +
            "        \"contour_right11\": {\n" +
            "          \"y\": 481,\n" +
            "          \"x\": 671\n" +
            "        },\n" +
            "        \"contour_right10\": {\n" +
            "          \"y\": 463,\n" +
            "          \"x\": 681\n" +
            "        },\n" +
            "        \"left_eye_upper_right_quarter\": {\n" +
            "          \"y\": 303,\n" +
            "          \"x\": 515\n" +
            "        },\n" +
            "        \"contour_right12\": {\n" +
            "          \"y\": 497,\n" +
            "          \"x\": 659\n" +
            "        },\n" +
            "        \"left_eye_lower_right_quarter\": {\n" +
            "          \"y\": 318,\n" +
            "          \"x\": 512\n" +
            "        },\n" +
            "        \"mouth_lower_lip_top\": {\n" +
            "          \"y\": 463,\n" +
            "          \"x\": 567\n" +
            "        },\n" +
            "        \"right_eye_upper_left_quarter\": {\n" +
            "          \"y\": 301,\n" +
            "          \"x\": 617\n" +
            "        },\n" +
            "        \"right_eye_pupil\": {\n" +
            "          \"y\": 305,\n" +
            "          \"x\": 634\n" +
            "        }\n" +
            "      },\n" +
            "      \"attributes\": {\n" +
            "        \"headpose\": {\n" +
            "          \"yaw_angle\": 4.054522,\n" +
            "          \"pitch_angle\": 5.4693522,\n" +
            "          \"roll_angle\": -0.0750022\n" +
            "        }\n" +
            "      },\n" +
            "      \"face_token\": \"3747333c5642715554c5bcd057d43082\",\n" +
            "      \"face_rectangle\": {\n" +
            "        \"width\": 301,\n" +
            "        \"top\": 246,\n" +
            "        \"height\": 301,\n" +
            "        \"left\": 417\n" +
            "      }\n" +
            "    }\n" +
            "  ],\n" +
            "  \"request_id\": \"1552388457,e745b36c-2d6b-4335-841c-83ba1e576888\"\n" +
            "}" ;


    private final JSONObject landmarks ;
    private int arrayIndex = 0;
    private int[] points = new int[106 * 2] ;

    public FacePointParser(JSONObject jsonObject) {
        JSONArray faces = jsonObject.optJSONArray("faces") ;
        if ( faces != null && faces.length() > 0 ) {
            this.landmarks = faces.optJSONObject(0).optJSONObject("landmark");
        } else {
            this.landmarks = new JSONObject() ;
        }
    }

    private void readPointData(String dataKey) {
        JSONObject jsonData = landmarks.optJSONObject(dataKey) ;
        points[arrayIndex++] = jsonData != null ? jsonData.optInt("x") : -1;
        points[arrayIndex++] = jsonData != null ? jsonData.optInt("y") : -1 ;
    }

    public void parse() {
        // 0 ~ 15
        for (int i = 0; i < 16; i++) {
            readPointData("contour_left" + (i + 1));
        }

        // 16
        readPointData("contour_chin");
        // 17 ~ 32
        for (int i = 16; i > 0; i--) {
            readPointData("contour_right" + i);
        }

        // 33
        readPointData("left_eyebrow_left_corner");
        // 34
        readPointData("left_eyebrow_upper_left_quarter");
        // 35
        readPointData("left_eyebrow_upper_middle");
        // 36
        readPointData("left_eyebrow_upper_right_quarter");
        // 37
        readPointData("left_eyebrow_upper_right_corner");
        // 38
        readPointData("right_eyebrow_upper_left_corner");
        // 39
        readPointData("right_eyebrow_upper_left_quarter");
        // 40
        readPointData("right_eyebrow_upper_middle");
        // 41
        readPointData("right_eyebrow_upper_right_quarter");
        // 42
        readPointData("right_eyebrow_right_corner");
        // 43 ~ 45
        for (int i = 0; i < 3; i++) {
            readPointData("nose_bridge" + (i + 1));
        }

        // 46
        readPointData("nose_tip");
        // 47
        readPointData("nose_left_contour4");
        // 48
        readPointData("nose_left_contour5");
        // 49
        readPointData("nose_middle_contour");
        // 50
        readPointData("nose_right_contour5");
        // 51
        readPointData("nose_right_contour4");
        // 52
        readPointData("left_eye_left_corner");
        // 53
        readPointData("left_eye_upper_left_quarter");
        // 54
        readPointData("left_eye_upper_right_quarter");
        // 55
        readPointData("left_eye_right_corner");
        // 56
        readPointData("left_eye_lower_right_quarter");
        // 57
        readPointData("left_eye_lower_left_quarter");
        // 58
        readPointData("right_eye_left_corner");
        // 59
        readPointData("right_eye_upper_left_quarter");
        // 60
        readPointData("right_eye_upper_right_quarter");
        // 61
        readPointData("right_eye_right_corner");
        // 62
        readPointData("right_eye_lower_right_quarter");
        // 63
        readPointData("right_eye_lower_left_quarter");
        // 64
        readPointData("left_eyebrow_lower_left_quarter");
        // 65
        readPointData("left_eyebrow_lower_middle");
        // 66
        readPointData("left_eyebrow_lower_right_quarter");
        // 67
        readPointData("left_eyebrow_lower_right_corner");
        // 68
        readPointData("right_eyebrow_lower_left_corner");
        // 69
        readPointData("right_eyebrow_lower_left_quarter");
        // 70
        readPointData("right_eyebrow_lower_middle");
        // 71
        readPointData("right_eyebrow_lower_right_quarter");
        // 72
        readPointData("left_eye_top");
        // 73
        readPointData("left_eye_bottom");
        // 74
        readPointData("left_eye_center");

        // 75
        readPointData("right_eye_top");
        // 76
        readPointData("right_eye_bottom");
        // 77
        readPointData("right_eye_center");
        // 78
        readPointData("nose_left_contour1");
        // 79
        readPointData("nose_right_contour1");
        // 80
        readPointData("nose_left_contour2");
        // 81
        readPointData("nose_right_contour2");
        // 82
        readPointData("nose_left_contour3");
        // 83
        readPointData("nose_right_contour3");
        // 84
        readPointData("mouth_left_corner");
        // 85
        readPointData("mouth_upper_lip_left_contour2");
        // 86
        readPointData("mouth_upper_lip_left_contour1");
        // 87
        readPointData("mouth_upper_lip_top");

        // 88
        readPointData("mouth_upper_lip_right_contour1");
        // 89
        readPointData("mouth_upper_lip_right_contour2");
        // 90
        readPointData("mouth_right_corner");

        // 91
        readPointData("mouth_lower_lip_right_contour2");
        // 92
        readPointData("mouth_lower_lip_right_contour3");
        // 93
        readPointData("mouth_lower_lip_bottom");
        // 94
        readPointData("mouth_lower_lip_left_contour3");
        // 95
        readPointData("mouth_lower_lip_left_contour2");
        // 96
        readPointData("mouth_upper_lip_left_contour3");

        // 97
        readPointData("mouth_upper_lip_left_contour4");
        // 98
        readPointData("mouth_upper_lip_bottom");
        // 99
        readPointData("mouth_upper_lip_right_contour4");
        // 100
        readPointData("mouth_upper_lip_right_contour3");

        // 101
        readPointData("mouth_lower_lip_right_contour1");
        // 102
        readPointData("mouth_lower_lip_top");
        // 103
        readPointData("mouth_lower_lip_left_contour1");
        // 104
        readPointData("left_eye_pupil");
        // 105
        readPointData("right_eye_pupil");
    }

    public void showPoints() {
        for (int i = 0; i < points.length; i += 2) {
            Log.e("", "### (" + points[i]  + ", " + points[i + 1] + ")") ;
        }
    }
}
