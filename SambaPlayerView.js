/* eslint-disable react/require-default-props */
/* eslint-disable no-underscore-dangle */
/* eslint-disable no-undef */
/* eslint-disable react/destructuring-assignment */
import {
  requireNativeComponent,
  ViewPropTypes,
  UIManager,
  findNodeHandle,
} from 'react-native';

import PropTypes from 'prop-types';
import React from 'react';

class SambaPlayer extends React.PureComponent {
  /** Events */
  _topOnLoad = (event: Event) => {
    if (!this.props.onLoad) {
      console.log('onLoad Cancelled');
      return;
    }
    this.props.onLoad(event.nativeEvent);
  };

  _topOnPlay = (event: Event) => {
    if (!this.props.onPlay) {
      console.log('onPlay Cancelled');
      return;
    }
    this.props.onPlay(event.nativeEvent);
  };

  _topOnPause = (event: Event) => {
    if (!this.props.onPause) {
      console.log('onPause Cancelled');
      return;
    }
    this.props.onPause(event.nativeEvent);
  };

  _topOnProgress = (event: Event) => {
    if (!this.props.onProgress) {
      console.log('onProgress Cancelled');
      return;
    }
    this.props.onProgress(event.nativeEvent);
  };

  _topOnStop = (event: Event) => {
    if (!this.props.onStop) {
      console.log('onStop Cancelled');
      return;
    }
    this.props.onStop(event.nativeEvent);
  };

  _topOnFinish = (event: Event) => {
    if (!this.props.onFinish) {
      console.log('onFinish Cancelled');
      return;
    }
    this.props.onFinish(event.nativeEvent);
  };

  _topOnDestroy = (event: Event) => {
    if (!this.props.onDestroy) {
      console.log('onDestroy Cancelled');
      return;
    }
    this.props.onDestroy(event.nativeEvent);
  };

  _topOnFullscreen = (event: Event) => {
    if (!this.props.onFullscreen) {
      console.log('onFullscreen Cancelled');
      return;
    }
    this.props.onFullscreen(event.nativeEvent);
  };

  _topOnFullscreenExit = (event: Event) => {
    if (!this.props.onFullscreenExit) {
      console.log('onFullscreenExit Cancelled');
      return;
    }
    this.props.onFullscreenExit(event.nativeEvent);
  };

  _topOnCastConnect = (event: Event) => {
    if (!this.props.onCastConnect) {
      console.log('onCastConnect Cancelled');
      return;
    }
    this.props.onCastConnect(event.nativeEvent);
  };

  _topOnCastDisconnect = (event: Event) => {
    if (!this.props.onCastDisconnect) {
      console.log('onCastDisconnect Cancelled');
      return;
    }
    this.props.onCastDisconnect(event.nativeEvent);
  };

  _topOnCastPlay = (event: Event) => {
    if (!this.props.onCastPlay) {
      console.log('onCastPlay Cancelled');
      return;
    }
    this.props.onCastPlay(event.nativeEvent);
  };

  _topOnCastPause = (event: Event) => {
    if (!this.props.onCastPause) {
      console.log('onCastPause Cancelled');
      return;
    }
    this.props.onCastPause(event.nativeEvent);
  };

  _topOnCastFinish = (event: Event) => {
    if (!this.props.onCastFinish) {
      console.log('onCastFinish Cancelled');
      return;
    }
    this.props.onCastFinish(event.nativeEvent);
  };

  _topOnDownloadRequestPrepared = (event: Event) => {
    if (!this.props.onDownloadRequestPrepared) {
      console.log('onDownloadRequestPrepared Cancelled');
      return;
    }
    this.props.onDownloadRequestPrepared(event.nativeEvent);
  };

  _topOnDownloadRequestPreparedFailed = (event: Event) => {
    if (!this.props.onDownloadRequestPreparedFailed) {
      console.log('_topOnDownloadRequestPreparedFailed Cancelled');
      return;
    }
    this.props.onDownloadRequestPreparedFailed(event.nativeEvent);
  };

  _topOnDownloadStateChanged = (event: Event) => {
    if (!this.props.onDownloadStateChanged) {
      console.log('_topOnDownloadStateChanged Cancelled');
      return;
    }
    this.props.onDownloadStateChanged(event.nativeEvent);
  };

  /* Commands */

  /**
   * Carrega a media e faz o autoPlay conforme parâmetro informado.
   */
  loadMedia = (autoPlay: Boolean = false) => {
    console.log('loadMedia invoked');
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.getViewManagerConfig('SambaPlayerView').Commands.loadMedia,
      [autoPlay]
    );
  };

  play = () => {
    console.log('play invoked');
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.getViewManagerConfig('SambaPlayerView').Commands.play,
      []
    );
  };

  pause = () => {
    console.log('pause');
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.getViewManagerConfig('SambaPlayerView').Commands.pause,
      []
    );
  };

  stop = () => {
    console.log('stop');
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.getViewManagerConfig('SambaPlayerView').Commands.stop,
      []
    );
  };

  prepareDownload = () => {
    console.log('prepareDownload');
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.getViewManagerConfig('SambaPlayerView').Commands
        .prepareDownload,
      []
    );
  };

  performDownload = selectedVideoSizeForDownnload => {
    console.log('performDownload');
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      UIManager.getViewManagerConfig('SambaPlayerView').Commands
        .performDownload,
      [selectedVideoSizeForDownnload]
    );
  };

  render() {
    const nativeProps = {
      ...this.props,
      topOnLoad: this._topOnLoad,
      topOnPlay: this._topOnPlay,
      topOnPause: this._topOnPause,
      topOnProgress: this._topOnProgress,
      topOnStop: this._topOnStop,
      topOnFinish: this._topOnFinish,
      topOnDestroy: this._topOnDestroy,
      topOnFullscreen: this._topOnFullscreen,
      topOnFullscreenExit: this._topOnFullscreenExit,
      topOnCastConnect: this._topOnCastConnect,
      topOnCastDisconnect: this._topOnCastDisconnect,
      topOnCastPlay: this._topOnCastPlay,
      topOnCastPause: this._topOnCastPause,
      topOnCastFinish: this._topOnCastFinish,
      topOnDownloadRequestPrepared: this._topOnDownloadRequestPrepared,
      topOnDownloadRequestPreparedFailed: this
        ._topOnDownloadRequestPreparedFailed,
      topOnDownloadStateChanged: this._topOnDownloadStateChanged,
    };
    return <SambaPlayerView {...nativeProps} />;
  }
}

const SambaPlayerView = requireNativeComponent('SambaPlayerView', SambaPlayer);

SambaPlayer.propTypes = {
  mediaIdOrLiveChannelId: PropTypes.string,
  projectHash: PropTypes.string,
  accessToken: PropTypes.string,
  mediaTitle: PropTypes.string,
  startAtIndex: PropTypes.number,
  onLoad: PropTypes.func,
  onPlay: PropTypes.func,
  onPause: PropTypes.func,
  onProgress: PropTypes.func,
  onStop: PropTypes.func,
  onFinish: PropTypes.func,
  onDestroy: PropTypes.func,
  onFullscreen: PropTypes.func,
  onFullscreenExit: PropTypes.func,
  onCastConnect: PropTypes.func,
  onCastDisconnect: PropTypes.func,
  onCastPlay: PropTypes.func,
  onCastPause: PropTypes.func,
  onCastFinish: PropTypes.func,
  ...ViewPropTypes,
};

module.exports = SambaPlayer;
