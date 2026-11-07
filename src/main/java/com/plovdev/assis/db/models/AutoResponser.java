package com.plovdev.assis.db.models;

import java.util.Map;

public class AutoResponser {
    private String userId;
    private float temeprature;
    private int maxTokens;
    private float top_p;
    private float repeat_penalty;
    private String systemPrompt;
    private Map<String, String> history;

    public AutoResponser(String userId, float temeprature, int maxTokens, float top_p, float repeat_penalty, String systemPrompt, Map<String, String> history) {
        this.userId = userId;
        this.temeprature = temeprature;
        this.maxTokens = maxTokens;
        this.top_p = top_p;
        this.repeat_penalty = repeat_penalty;
        this.systemPrompt = systemPrompt;
        this.history = history;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public float getTemeprature() {
        return temeprature;
    }

    public void setTemeprature(float temeprature) {
        this.temeprature = temeprature;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public float getTop_p() {
        return top_p;
    }

    public void setTop_p(float top_p) {
        this.top_p = top_p;
    }

    public float getRepeat_penalty() {
        return repeat_penalty;
    }

    public void setRepeat_penalty(float repeat_penalty) {
        this.repeat_penalty = repeat_penalty;
    }

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public void setSystemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }

    public Map<String, String> getHistory() {
        return history;
    }

    public void setHistory(Map<String, String> history) {
        this.history = history;
    }
}