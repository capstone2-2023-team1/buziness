package com.example.nerfbiz;

public interface Constant {

    String FILE_PATH_RESOURCES = System.getProperty("user.dir")+"\\src\\main\\resources\\";
    String FILE_PATH_STATIC = FILE_PATH_RESOURCES+"static\\";
    String FILE_PATH = FILE_PATH_STATIC+"videos\\";
    String FRONT_SERVER_PATH = "";
    String FUNCTIONAL_SERVER_PATH = "http://127.0.0.1:8000/";
    String FUNCTIONAL_SERVER_PATH_VIDEO2TRD = FUNCTIONAL_SERVER_PATH+"trans/";
}