package com.example.gamesample;
 
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
 
public class GameSample extends Activity {
 
  @Override
	public void onCreate( Bundle savedInstanceState ) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate( savedInstanceState );
		// 描画クラスのインスタンスを生成
		MySurfaceView mSurfaceView = new MySurfaceView(this);
		setContentView(mSurfaceView);
	}
}

class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback{
	/** 画面サイズ */
	private int w;
	private int h;
	/** 自機であるねこに関する情報を保持するクラス */
	private Cat cat;
	
	public MySurfaceView(Context context) {
		super(context);
		// イベント取得できるようにFocusを有効にする
		setFocusable( true );

		// Callbackを登録する
		getHolder().addCallback(this);

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		//画面サイズの保持
		this.w = width;
		this.h = height;
		// Resourceインスタンスの生成
		Resources res = this.getContext().getResources();
		//ねこの初期化
		Bitmap catImage = BitmapFactory.decodeResource(res, R.drawable.cat);
		cat = new Cat(catImage, w, h);
		//画像のメモリ解放
		if (catImage.isRecycled()) {
			catImage.recycle();
		}
		//ゲーム画面の描画
		drawGame();
	}


	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	}


	/**
	 * タッチイベント
	 */
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			int x = (int) event.getX();
			int y = (int) event.getY();
			//ねこの移動させる位置を設定
			cat.move(x, y);
			drawGame();
		}
		return true;
	}
	
	/**
	 * 太鼓の画像を描画するメソッド
	 */
	private void drawGame() {
		// Canvasを取得する
		Canvas canvas = getHolder().lockCanvas();

		// 背景色を設定する
		canvas.drawColor( Color.BLUE );
 
		// Bitmapイメージの描画
		Paint mPaint = new Paint();
		mPaint.setColor(Color.BLACK);
		//穴の表示
		canvas.drawCircle(w/2, h/2, 30, mPaint);
		//ねこを動かす
		cat.drawMove(canvas);
		// 画面に描画をする
		getHolder().unlockCanvasAndPost(canvas);		
	}
	
}

class Cat {
	/** ねこの画像を保持する */
	private Bitmap catImage;
	/** ねこが表示されているx,y座標を保持する */
	private Point p;
	/** 画面サイズを超えることを判定するために保持 */
	private Point disp;
	
	/** コンストラクタ */
	public Cat(Bitmap catImage, int w, int h) {
		this.catImage = catImage;
		p = new Point();
		disp = new Point();
		disp.x = w;
		disp.y = h;
		init(w, h);
	}
	/** ねこの動きを初期化する */
	private void init(int w, int h) {
		//ねこの初期値
		p.x = w / 2;
		p.y = h - catImage.getHeight();
	}
	/** ねこを移動させる位置を表示 */
	public void move(int x, int y) {
		p.x = x;
		p.y = y;
	}
	/** ねこの動きを描画するクラス */
	public void drawMove(Canvas c) {
		c.drawBitmap(catImage, p.x, p.y, new Paint());
	}
}