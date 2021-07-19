package com.rnsambatechplayersdk;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.facebook.react.ReactActivity;
import com.sambatech.player.SambaPlayer;
import com.sambatech.player.cast.SambaCast;
import com.sambatech.player.offline.SambaDownloadManager;

public class MainActivity extends ReactActivity {

  private static Activity _instance = null;
  private static SambaCast _sambaCast;
  private static SambaPlayer _player = null;

  public MainActivity(){
    super();
    _instance = this;
  }

  public static SambaCast getSambaCastInstance() {
    return _sambaCast;
  }

  public static void setPlayerInstance(SambaPlayer player) {
    _player = player;
  }

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "RNSambatechPlayerSdk";
  }

  static Activity getInstance(){
    return _instance;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    _sambaCast = new SambaCast(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    if(_sambaCast!= null){
      _sambaCast.notifyActivityResume();
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    if(_sambaCast!= null){
      _sambaCast.notifyActivityPause();
    }

    // pauses player when pausing activity, unless when casting,
    // otherwise casting would get paused.
    if (_player != null && _player.hasStarted() && !_sambaCast.isCasting())
      _player.pause();
  }
}
