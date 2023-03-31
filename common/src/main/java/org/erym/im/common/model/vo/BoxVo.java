package org.erym.im.common.model.vo;


/**
 * @author cjt
 * @date 2021/6/21 9:46
 */
public class BoxVo {

    private UserVo userVo;


    /**
     * 类型（0私聊，1群聊）
     */
    private boolean type;

    public UserVo getUserVo() {
        return userVo;
    }

    public void setUserVo(UserVo userVo) {
        this.userVo = userVo;
    }


    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BoxVo{" +
                "userVo=" + userVo +
                ", type=" + type +
                '}';
    }
}
