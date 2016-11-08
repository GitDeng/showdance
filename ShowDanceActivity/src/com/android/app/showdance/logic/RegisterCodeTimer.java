package com.android.app.showdance.logic;

import android.os.CountDownTimer;
import android.os.Handler;

/**
 * 注册验证码计时器
 * 
 * @author 
 * 
 */
public class RegisterCodeTimer extends CountDownTimer {
	private static Handler mHandler;
	public static final int IN_RUNNING = 1001;
	public static int END_RUNNING = 1002;

	/**
	 * @param millisInFuture
	 *            // 倒计时的时长
	 * @param countDownInterval
	 *            // 间隔时间
	 * @param handler
	 *            // 通知进度的Handler
	 */
	public RegisterCodeTimer(long millisInFuture, long countDownInterval, Handler handler) {
		super(millisInFuture, countDownInterval);
		mHandler = handler;
	}

	// 结束
	@Override
	public void onFinish() {
		if (mHandler != null)
			mHandler.obtainMessage(END_RUNNING, "获取验证码").sendToTarget();
	}

	@Override
	public void onTick(long millisUntilFinished) {
		if (mHandler != null)
			mHandler.obtainMessage(IN_RUNNING, (millisUntilFinished / 1000) + " s后重发").sendToTarget();
	}

}
