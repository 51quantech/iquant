package com.iquant.base;

/**
 * Created by yonggangli on 2016/8/29.
 */
public class Start extends AbstractServer{

    public Start(String[] anArgs) {
        super(anArgs);
    }

    public static void main(String... anArgs) throws Exception {
        System.out.println("*****************************"+anArgs.length);
        for(int i=0;i<anArgs.length;i++){
            System.out.println("********************"+anArgs[i]);
        }
        new Start(anArgs).run();
    }

    @Override
    public void init(Config config) {
        config.setMin_thread(128);
        config.setMax_thread(512);
    }
}
