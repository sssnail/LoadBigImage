package com.sll.loadbigimageapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

//    @BindView(R.id.imageView)
//    BigImageView bigImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BigImageView bigImageView = findViewById(R.id.imageView);
        InputStream is = null;
        try {
            is = getAssets().open("image.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        bigImageView.setImage(is);

        String imageUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1569732951694&di=d26f786b12da70001e3eb6c89e14881c&imgtype=0&src=http%3A%2F%2Fwx3.sinaimg.cn%2Forj360%2F006qjkdngy1g50udtzemoj30ii39bteh.jpg";
//        Utils.imageLoaderDisplayImage(this, imageUrl, (ImageView) bigImageView);
    }
}
