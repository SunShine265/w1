package com.wo1haitao.models;

import com.wo1haitao.api.response.UserProfile;

/**
 * Created by user on 6/28/2017.
 */

public class QuestionReplyModel {
    long id, question_answer_id, common_user_id;
    String content;
    UserProfile common_user;
    String created_at;
    QuestionAnswer question_answer;
    long idOfQuestion;
    boolean isLastReply;
    String flag;

    public String getFlag() {
        if(flag == null){
            return "";
        }
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public long getIdOfQuestion() {
        return idOfQuestion;
    }

    public void setIdOfQuestion(long idOfQuestion) {
        this.idOfQuestion = idOfQuestion;
    }

    public boolean isLastReply() {
        return isLastReply;
    }

    public void setLastReply(boolean lastReply) {
        isLastReply = lastReply;
    }

    public String getCreated_at() {
        if(created_at == null){
            return "";
        }
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public UserProfile getCommon_user() {
        if(common_user == null){
            return new UserProfile();
        }
        return common_user;
    }

    public void setCommon_user(UserProfile common_user) {
        this.common_user = common_user;
    }

    public QuestionAnswer getQuestion_answer() {
        return question_answer;
    }

    public void setQuestion_answer(QuestionAnswer question_answer) {
        this.question_answer = question_answer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getQuestion_answer_id() {
        return question_answer_id;
    }

    public void setQuestion_answer_id(long question_answer_id) {
        this.question_answer_id = question_answer_id;
    }

    public long getCommon_user_id() {
        return common_user_id;
    }

    public void setCommon_user_id(long common_user_id) {
        this.common_user_id = common_user_id;
    }

    public String getContent() {
        if(content == null){
            return "";
        }
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
