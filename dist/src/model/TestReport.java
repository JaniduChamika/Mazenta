/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ROG STRIX
 */
public class TestReport {
    private String topic;
    private String test;
    private String result;
    private String normalvalue;

    public TestReport(String topic, String test, String result, String normalvalue) {
        this.topic = topic;
        this.test = test;
        this.result = result;
        this.normalvalue = normalvalue;
    }

    
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getNormalvalue() {
        return normalvalue;
    }

    public void setNormalvalue(String normalvalue) {
        this.normalvalue = normalvalue;
    }
}
