package ea.qa.joohan.lunchpick;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    /**
     * 버그 수정 해야됨 ...
     * 1. 엔터 누르면 추가되기(Add button처럼)
     * 2. MainActivity에서 Go back누르면 종료되거나 뒤로가지거나 랜덤으로 됨
     * 3. 애니메이션 안멈춤
     * 4.Next버튼 위로 따라올라옴(의도하진 않았지만 괜찮으면 그냥 안고치기)
     * 5.럭키박스 (알파넣은거) 좀 더 작게 자르기
     *
     *
     */

    private List<Ticket> tickets;
    private Dialog createResultDialog;
    private ImageView luckyBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        tickets = intent.getParcelableArrayListExtra("tickets");
        luckyBox = findViewById(R.id.lucky_box);
        shakeMotion(luckyBox);

        luckyBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                luckyBox.clearAnimation();
                luckyBox.invalidate();
                int selected = (int)(Math.random()*tickets.size());
                createResultDialog = createDialog(selected);
                createResultDialog.show();
            }
        });



    }

    private Dialog createDialog(int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(tickets.get(i).getName());
        builder.setPositiveButton(getContext().getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                shakeMotion(luckyBox);
            }
        });
        builder.setNegativeButton(getContext().getResources().getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.exit(0);
            }
        });
        return builder.create();

    }


    private void shakeMotion (final ImageView imageView) {

        TranslateAnimation toTheRight = new TranslateAnimation(0.0f, 50.0f,
                0.0f, 50.0f);
        TranslateAnimation toTheLeft = new TranslateAnimation(0.0f, -50.0f,
                0.0f, -50.0f);
        TranslateAnimation toUp = new TranslateAnimation(0.0f, 50.0f,
                0.0f, -50.0f);
        TranslateAnimation toDown = new TranslateAnimation(0.0f, -50.0f,
                0.0f, 50.0f);

        ArrayList<TranslateAnimation> movings = new ArrayList<>();
        movings.add(toTheLeft);
        movings.add(toTheRight);
        movings.add(toUp);
        movings.add(toDown);
        for(TranslateAnimation a : movings) {
            a.setDuration(100);
            a.setRepeatMode(2);
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    shakeMotion(imageView);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
                    int rand = (int) (Math.random() * 4);
                    switch (rand) {
                        case 0:
                            imageView.startAnimation(toTheLeft);
                            Log.d("Animation","TotheLeft");
                            break;
                        case 1:
                            imageView.startAnimation(toTheRight);
                            Log.d("Animation","TotheRight");
                            break;
                        case 2:
                            imageView.startAnimation(toUp);
                            Log.d("Animation","ToUp");
                            break;
                        case 3:
                            imageView.startAnimation(toDown);
                            Log.d("Animation","ToDown");
                            break;
                    }
    }
    private Context getContext() {
        return this;
    }
}
