package xiaofei.com.festival_sms.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaofei on 2015/11/15.
 */
public class FestivalLab {

    public static FestivalLab mInstance;
    private List<Festival> mFestivals = new ArrayList<Festival>() ;
    private List<Msg> mMsgs = new ArrayList<Msg>();

    private FestivalLab(){
        mFestivals.add(new Festival(1, "国庆节"));
        mFestivals.add(new Festival(2, "中秋节"));
        mFestivals.add(new Festival(3, "元旦"));
        mFestivals.add(new Festival(4, "春节"));
        mFestivals.add(new Festival(5, "端午节"));
        mFestivals.add(new Festival(6, "七夕节"));
        mFestivals.add(new Festival(7, "圣诞节"));
        mFestivals.add(new Festival(8, "儿童节"));

        mMsgs.add(new Msg(1, 1, "祝你们永结同心，百年好合！新婚愉快，甜甜蜜蜜！ "));
        mMsgs.add(new Msg(2, 1, "你们本就是天生一对，地造一双，而今共偕连理，今后更需彼此宽容、互相照顾，祝福你们!\n" +
                "愿你俩恩恩爱爱，意笃情深，此生爱情永恒，爱心与日俱增！ "));
        mMsgs.add(new Msg(3, 1, "他是词，你是谱，你俩就是一首和谐的歌。天作之合，鸾凤和鸣。 "));
        mMsgs.add(new Msg(4, 1, "千禧年结千年缘，百年身伴百年眠。天生才子佳人配，只羡鸳鸯不羡仙。  "));
        mMsgs.add(new Msg(5, 1, "愿快乐的歌声永远伴你们同行，愿你们婚后的生活洋溢着喜悦与欢快，永浴于无穷的快乐年华。谨祝新婚快乐 "));
        mMsgs.add(new Msg(6, 1, "辛劳了半辈子，贡献了几十年，在这春暧花开的日子，恭贺您再婚之喜，正所谓“夕无限好，萱草晚来香”！"));
        mMsgs.add(new Msg(7, 1, "在这春暧花开、群芳吐艳的日子里，你俩永结同好，正所谓天生一对、地生一双！祝愿你俩恩恩爱爱，白头偕老！"));
        mMsgs.add(new Msg(8, 1, "愿天下有情人终成眷属，前生注定，喜结良缘。"));
        mMsgs.add(new Msg(9, 1, "新婚大喜！百年好合！"));
    }

    public List<Msg> getMsgByFestivalId(int fesId){
        List<Msg> msgs = new ArrayList<Msg>();
        for (Msg msg: mMsgs){
            if(msg.getFestivalId() == fesId){
                msgs.add(msg);
            }
        }
        return msgs;
    }

    public Msg getMsgById(int msgId){
        for(Msg msg : mMsgs){
            if(msg.getId() == msgId){
              return msg;
            }
        }
        return null;
    }

    public List<Festival> getFestivals (){
        return new ArrayList<Festival>(mFestivals);
    }

    public Festival getFestivalByID(int fesID) {
        for(Festival fes: mFestivals){
            if(fes.getId() == fesID){
                return fes;
            }
        }
        return null ;
    }

    public static FestivalLab getInstance(){
        if(mInstance == null){
            synchronized (FestivalLab.class){
                if (mInstance == null){
                    mInstance = new FestivalLab();
                }
            }
        }
        return mInstance;
    }
}
