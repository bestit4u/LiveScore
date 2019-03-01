package com.diretta.livescore;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jimmy-PC on 5/19/2016.
 */
public class TipResultDialog extends Dialog {
    private OnCancelOrderListener mListener;
    private EditText edtAmount;
    private TextView txtMatchName;
    private TextView txtMarketStyle;
    private TextView txtOddStyle;
    private String mMarketStyle;
    private String mMatchName;
    private String mOddStyle;
    public void setListener(OnCancelOrderListener listener) {
        mListener = listener;
    }

    public TipResultDialog(Context context) {
        super(context);
    }
     public void setMarketStyle(String marketStyle){
         mMarketStyle = marketStyle;
     }
    public void setMatchName(String matchName){
        mMatchName = matchName;
    }
    public void setOddStyle(String oddStyle){
        mOddStyle = oddStyle;
    }
    public TipResultDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public TipResultDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_tip);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setAttributes(params);
        findViewById(R.id.imgClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipResultDialog.this.dismiss();
            }
        });
        txtMarketStyle = (TextView)findViewById(R.id.txtMarketStyle);
        txtMarketStyle.setText(mMarketStyle);
        txtMatchName = (TextView)findViewById(R.id.txtMatchName);
        txtMatchName.setText(mMatchName);
        txtOddStyle = (TextView)findViewById(R.id.txtOddStyle);
        txtOddStyle.setText(mOddStyle);
        edtAmount = (EditText)findViewById(R.id.edtAmount);
        findViewById(R.id.btnPlaceTip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strAmount = edtAmount.getText().toString();
                if(!strAmount.equals("")){
                    int amount = Integer.parseInt(strAmount);
                    if(amount >= 10 && amount <= 2500){
                        mListener.OnCancel(strAmount);
                        TipResultDialog.this.dismiss();
                    }else{
                        Toast.makeText(getContext(), "You have to place the tip amount between 10 - 2500", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(), "Please input the Tip Amount!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public interface OnCancelOrderListener {
        void OnCancel(String strReason);
    }
}
