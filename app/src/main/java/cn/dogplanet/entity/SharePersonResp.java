package cn.dogplanet.entity;

/**
 * Created by zhangtianrui on 17/3/14.
 */
public class SharePersonResp extends Resp {

    private String inviteMan;

    private InviteArr inviteArr;

    public String getInviteMan() {
        return inviteMan;
    }

    public void setInviteMan(String inviteMan) {
        this.inviteMan = inviteMan;
    }

    public InviteArr getInviteArr() {
        return inviteArr;
    }

    public void setInviteArr(InviteArr inviteArr) {
        this.inviteArr = inviteArr;
    }

    public class InviteArr{
        private String invite_code;
        private String invite_count;
        private String check_status;

        public String getInvite_code() {
            return invite_code;
        }

        public void setInvite_code(String invite_code) {
            this.invite_code = invite_code;
        }

        public String getInvite_count() {
            return invite_count;
        }

        public void setInvite_count(String invite_count) {
            this.invite_count = invite_count;
        }

        public String getCheck_status() {
            return check_status;
        }

        public void setCheck_status(String check_status) {
            this.check_status = check_status;
        }
    }
}
