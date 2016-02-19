package com.software.eric.coolweather.beans.china;

/**
 * Created by Mzz on 2016/2/10.
 */
public class Suggestion {

    /**
     * brf : 较舒适
     * txt : 白天天气晴好，您在这种天气条件下，会感觉早晚凉爽、舒适，午后偏热。
     */

    private ComfEntity comf;
    /**
     * brf : 较不宜
     * txt : 较不宜洗车，未来一天无雨，风力较大，如果执意擦洗汽车，要做好蒙上污垢的心理准备。
     */

    private CwEntity cw;
    /**
     * brf : 炎热
     * txt : 天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。
     */

    private DrsgEntity drsg;
    /**
     * brf : 少发
     * txt : 各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。
     */

    private FluEntity flu;
    /**
     * brf : 较适宜
     * txt : 天气较好，但风力较大，推荐您进行室内运动，若在户外运动请注意防风。
     */

    private SportEntity sport;
    /**
     * brf : 适宜
     * txt : 天气较好，是个好天气哦。稍热但是风大，能缓解炎热的感觉，适宜旅游，可不要错过机会呦！
     */

    private TravEntity trav;
    /**
     * brf : 强
     * txt : 紫外线辐射强，建议涂擦SPF20左右、PA++的防晒护肤品。避免在10点至14点暴露于日光下。
     */

    private UvEntity uv;

    public void setComf(ComfEntity comf) {
        this.comf = comf;
    }

    public void setCw(CwEntity cw) {
        this.cw = cw;
    }

    public void setDrsg(DrsgEntity drsg) {
        this.drsg = drsg;
    }

    public void setFlu(FluEntity flu) {
        this.flu = flu;
    }

    public void setSport(SportEntity sport) {
        this.sport = sport;
    }

    public void setTrav(TravEntity trav) {
        this.trav = trav;
    }

    public void setUv(UvEntity uv) {
        this.uv = uv;
    }

    public ComfEntity getComf() {
        return comf;
    }

    public CwEntity getCw() {
        return cw;
    }

    public DrsgEntity getDrsg() {
        return drsg;
    }

    public FluEntity getFlu() {
        return flu;
    }

    public SportEntity getSport() {
        return sport;
    }

    public TravEntity getTrav() {
        return trav;
    }

    public UvEntity getUv() {
        return uv;
    }

    public static class ComfEntity {
        private String brf;
        private String txt;

        public void setBrf(String brf) {
            this.brf = brf;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        public String getBrf() {
            return brf;
        }

        public String getTxt() {
            return txt;
        }
    }

    public static class CwEntity {
        private String brf;
        private String txt;

        public void setBrf(String brf) {
            this.brf = brf;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        public String getBrf() {
            return brf;
        }

        public String getTxt() {
            return txt;
        }
    }

    public static class DrsgEntity {
        private String brf;
        private String txt;

        public void setBrf(String brf) {
            this.brf = brf;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        public String getBrf() {
            return brf;
        }

        public String getTxt() {
            return txt;
        }
    }

    public static class FluEntity {
        private String brf;
        private String txt;

        public void setBrf(String brf) {
            this.brf = brf;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        public String getBrf() {
            return brf;
        }

        public String getTxt() {
            return txt;
        }
    }

    public static class SportEntity {
        private String brf;
        private String txt;

        public void setBrf(String brf) {
            this.brf = brf;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        public String getBrf() {
            return brf;
        }

        public String getTxt() {
            return txt;
        }
    }

    public static class TravEntity {
        private String brf;
        private String txt;

        public void setBrf(String brf) {
            this.brf = brf;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        public String getBrf() {
            return brf;
        }

        public String getTxt() {
            return txt;
        }
    }

    public static class UvEntity {
        private String brf;
        private String txt;

        public void setBrf(String brf) {
            this.brf = brf;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        public String getBrf() {
            return brf;
        }

        public String getTxt() {
            return txt;
        }
    }
}
