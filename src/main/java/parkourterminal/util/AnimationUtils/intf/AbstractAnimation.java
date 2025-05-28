package parkourterminal.util.AnimationUtils.intf;

public abstract class AbstractAnimation<T extends interpolating<?,T>> {
    private final AnimationMode animationMode;
    private T start;
    private T end;
    private T new_;
    private float totalTime; // 运动时间 (秒)
    private float elapsedTime = 0; // 已经过的时间
    public AbstractAnimation(float totalTime, T start, AnimationMode animationMode){
        this.animationMode=animationMode;
        this.totalTime=totalTime;
        this.start=start;
        this.end=start;
        this.new_=start;
    }
    public void SetAnimationTime(float totalTime){
        this.totalTime=totalTime;
    }
    public void RestartAnimation(T end){
        if(this.end.equals(end)){
            return;
        }
        if(animationMode==AnimationMode.FORCED&&InAnimation()){
            return;
        }
        if(animationMode==AnimationMode.BLENDED){
            this.start=this.new_;
        }
        this.end=end;

        this.elapsedTime=0;
    }
    public T Update(){
        if (InAnimation()) {
            float t = elapsedTime / totalTime; // 归一化时间 (0 ~ 1)

            // 计算当前 progress
            float progress = this.ProgressFunction(t);

            // 计算直线插值
            new_=start.interpolate(end,progress);

            // 更新时间
            elapsedTime += 0.05f; // 每帧更新
            return new_;
        }
        new_=end;
        return end;
    }
    public T getInterpolatingValue(){
        return new_;
    }

    public float getProgress() {
        return elapsedTime/totalTime;
    }

    public boolean InAnimation(){
        return elapsedTime < totalTime;
    }
    //    public abstract float speedFunction(float t);
    public abstract float ProgressFunction(float t);
    public void changeWithOutAnimation(T p){
        if(animationMode==AnimationMode.OVERRIDEABLE){
            this.start=p;
            this.end=p;
            this.new_=p;
        }else if(animationMode==AnimationMode.FORCED){
            return;
        }else if(animationMode==AnimationMode.BLENDED){
            this.start=p;
            RestartAnimation(this.end);
        }
    };
}

