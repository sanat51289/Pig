package com.example.pig;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	protected TextView mPlayerScore_1;
	protected TextView mPlayerScore_2;
	protected EditText mPlayer_1_name;
	protected EditText mPlayer_2_name;
	protected ImageView mDiceImage;
	
	protected boolean isPlayer1 = true;
	private int mDiceVal;
	private int [] mImageRes = {
			R.drawable.dice1,
			R.drawable.dice2,
			R.drawable.dice3,
			R.drawable.dice4,
			R.drawable.dice5,
			R.drawable.dice6
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mPlayerScore_1 = (TextView) findViewById(R.id.playerScore_1);
		mPlayerScore_2 = (TextView) findViewById(R.id.playerScore_2);
		mPlayer_1_name = (EditText) findViewById(R.id.Player1);
		mPlayer_2_name = (EditText) findViewById(R.id.Player2);
		mDiceImage = (ImageView) findViewById(R.id.imageView1);
		
		mPlayer_1_name.setTextColor(Color.RED);
		mPlayer_1_name.requestFocus();
		mPlayer_2_name.setTextColor(Color.WHITE);
		
		Button rollButton = (Button) findViewById(R.id.rollButton);
		Button holdButton = (Button) findViewById(R.id.holdButton);
		
		rollButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mDiceVal = 1 + (int)(Math.random() * ((6 - 1) + 1));//since dice can have values between 1 & 6
				mDiceImage.setImageResource(mImageRes[mDiceVal-1]);
				if(mDiceVal == 1){
					//then hand over to the other player and nothing is added to this players points
					alertOne("");
				} else{
					updateScoreCard();
					String winner = checkWinner();
					if(winner != null && !winner.isEmpty()){
						alertWinner(winner);
					}
				}//end of else
			}//end of OnClick

		});
		
		holdButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//if you roll a one before holding then you lose all the points.
				if(mDiceVal != 1){
					String winner = checkWinner();
					if(winner != null && !winner.isEmpty()){
						alertWinner(winner);
					}
					else{
						passOver();
					}
				} else{ //the dice rolled 1 and hold button was pressed
					if(isPlayer1){
						mPlayerScore_1.setText(0+"");
						alertOne("You rolled one before hold, " +
								"you lose all points and now pass the turn to the other player ");
					} else{
						mPlayerScore_2.setText(0+"");
						alertOne("You rolled one before hold, " +
								"you lose all points and now pass the turn to the other player ");
					}
				}
			}

		});	
	}//end of onCreate
	
	private String checkWinner() {
		int score1 = Integer.parseInt(mPlayerScore_1.getText().toString());
		int score2 = Integer.parseInt(mPlayerScore_2.getText().toString());
		if(score1 >= 10 && score2 < 10){
			return "player1";
		}else if(score2 >= 10 && score1 < 10) {
			return "player2";
		} else{
			return null;
		}
	}
	
	private void alertOne(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setPositiveButton(android.R.string.ok, null);
		if(msg.isEmpty())
			builder.setTitle(R.string.you_rolled_one);
		else{
			builder.setTitle(msg);
		}
		AlertDialog dialog = builder.create();
		dialog.show();
		if(isPlayer1){
			isPlayer1 = false;
			mPlayer_2_name.requestFocus();
			mPlayer_1_name.setTextColor(Color.WHITE);
			mPlayer_2_name.setTextColor(Color.RED);
		} else{
			isPlayer1 = true;
			mPlayer_1_name.requestFocus();
			mPlayer_1_name.setTextColor(Color.RED);
			mPlayer_2_name.setTextColor(Color.WHITE);
		}
	}
	
	private void passOver() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
			.setPositiveButton(android.R.string.ok, null);
		if(isPlayer1){
			builder.setTitle("Switch to player 2");
			builder.setPositiveButton(android.R.string.ok, null);
			isPlayer1 = false;
			mPlayer_2_name.requestFocus();
			mPlayer_1_name.setTextColor(Color.WHITE);
			mPlayer_2_name.setTextColor(Color.RED);
		} else{
			builder.setTitle("Switch to player 1");
			builder.setPositiveButton(android.R.string.ok, null);
			isPlayer1 = true;
			mPlayer_1_name.setTextColor(Color.RED);
			mPlayer_2_name.setTextColor(Color.WHITE);
			mPlayer_1_name.requestFocus();
		}
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private void alertWinner(String winner) {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
		if(winner.equals("player1")){
			builder1.setTitle(mPlayer_1_name.getText().toString()+" wins!");
			builder1.setNegativeButton("Play again?", new ResetListener());
			builder1.setPositiveButton("Exit", new ExitListener());
			AlertDialog dialog1 = builder1.create();
			dialog1.show();
		} else if(winner.equals("player2")){
			builder1.setTitle(mPlayer_2_name.getText().toString()+" wins!");
			builder1.setNegativeButton("Play again?", new ResetListener());
			builder1.setPositiveButton("Exit", new ExitListener());
			AlertDialog dialog1 = builder1.create();
			dialog1.show();
		}
	}
	
	private void updateScoreCard() {
		if(isPlayer1){
			int playerS_1 = Integer.parseInt(mPlayerScore_1.getText().toString());
			mPlayerScore_1.setText((playerS_1 + mDiceVal)+"");
		} else{
			int playerS_2 = Integer.parseInt(mPlayerScore_2.getText().toString());
			mPlayerScore_2.setText((playerS_2 + mDiceVal)+"");
		}
	}
	
	private class ResetListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			Intent intent = new Intent(MainActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}//end of ResetListener
	
	private class ExitListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			finish();
		}
	}//end of ResetListener
}
