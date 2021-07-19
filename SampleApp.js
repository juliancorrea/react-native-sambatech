/* eslint-disable no-return-assign */
/* eslint-disable no-use-before-define */
/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React from 'react';
import {
  StyleSheet,
  View,
  StatusBar,
  Button,
  Modal,
  TouchableHighlight,
  Text,
  Alert,
} from 'react-native';

import SambaPlayer from './SambaPlayerView';

export default class App extends React.Component {
  player = null;

  constructor(props) {
    super(props);
    this.state = {
      videosOfflineAvailable: null,
      modalVisible: false,
      selectedVideoSizeForDownnload: -1,
    };
  }

  componentDidMount() {
    const autoPlay = true;
    if (this.player != null) this.player.loadMedia(autoPlay);
  }

  componentDidUpdate() {
    const { selectedVideoSizeForDownnload } = this.state;
    if (selectedVideoSizeForDownnload > -1) {
      this.player.performDownload(selectedVideoSizeForDownnload);
    }
  }

  showDownloadAvailables = () => {
    const { modalVisible, videosOfflineAvailable } = this.state;
    console.log('showDownloadAvailables====>', videosOfflineAvailable);
    return (
      <View style={styles.centeredView}>
        <Modal
          animationType="slide"
          transparent
          visible={modalVisible}
          onRequestClose={() => {
            // Alert.alert('Modal has been closed.');
          }}
        >
          <View style={styles.modalView}>
            {videosOfflineAvailable.videotracks.map((item, index) => {
              return (
                <TouchableHighlight
                  key={index}
                  style={styles.item}
                  onPress={() => {
                    this.setState({
                      modalVisible: !modalVisible,
                      selectedVideoSizeForDownnload: index,
                    });
                  }}
                >
                  <Text style={styles.modalText}>
                    {index} - {item.title}
                  </Text>
                </TouchableHighlight>
              );
            })}
          </View>
          <TouchableHighlight
            style={{ ...styles.openButton, backgroundColor: '#2196F3' }}
            onPress={() => {
              this.setState({
                modalVisible: false,
              });
            }}
          >
            <Text style={[styles.modalText, styles.textStyle]}>Fechar</Text>
          </TouchableHighlight>
        </Modal>
      </View>
    );
  };

  render = () => {
    if (this.state.modalVisible) return this.showDownloadAvailables();

    return (
      <>
        <StatusBar barStyle="dark-content" />
        <View style={styles.container}>
          <SambaPlayer
            style={styles.overLay}
            ref={r => (this.player = r)}
            mediaIdOrLiveChannelId=""
            projectHash=""
            accessToken=""
            startAtIndex={0}
            // oculta o titulo da midia
            mediaTitle=""
            styles={styles.overLay}
            onLoad={event =>
              console.log(`OnLoad RN => ${JSON.stringify(event)}`)
            }
            onPlay={event => console.log(`onPlay => ${JSON.stringify(event)}`)}
            onPause={event =>
              console.log(`onPause RN => ${JSON.stringify(event)}`)
            }
            onProgress={event =>
              console.log(`onProgress RN => ${JSON.stringify(event)}`)
            }
            onStop={event =>
              console.log(`onStop RN => ${JSON.stringify(event)}`)
            }
            onFinish={event =>
              console.log(`onFinish RN => ${JSON.stringify(event)}`)
            }
            onDestroy={event =>
              console.log(`onDestroy RN => ${JSON.stringify(event)}`)
            }
            onFullscreen={event =>
              console.log(`onFullscreen RN => ${JSON.stringify(event)}`)
            }
            onFullscreenExit={event =>
              console.log(`onFullscreenExit RN => ${JSON.stringify(event)}`)
            }
            onCastConnect={event =>
              console.log(`onCastConnect RN => ${JSON.stringify(event)}`)
            }
            onCastDisconnect={event =>
              console.log(`onCastDisconnect RN => ${JSON.stringify(event)}`)
            }
            onCastPlay={event =>
              console.log(`onCastPlay RN => ${JSON.stringify(event)}`)
            }
            onCastPause={event =>
              console.log(`onCastPause RN => ${JSON.stringify(event)}`)
            }
            onCastFinish={event =>
              console.log(`onCastFinish RN => ${JSON.stringify(event)}`)
            }
            onDownloadRequestPrepared={event => {
              console.log(
                `onDownloadRequestPrepared RN => ${JSON.stringify(event)}`
              );
              this.setState({
                videosOfflineAvailable: JSON.parse(event.data),
                modalVisible: true,
              });
            }}
            onDownloadRequestPreparedFailed={event => {
              console.log(
                `onDownloadRequestPreparedFailed RN => ${JSON.stringify(event)}`
              );
              Alert.alert(event.message);
            }}
            onDownloadStateChanged={event => {
              const data = JSON.parse(event.data);
              console.log(
                `onDownloadStateChanged RN => MediaTitle => ${
                  data.downloadData.mediaTitle
                } |  ${data.state} ${
                  data.downloadPercentage
                }% | totalDownloadSizeInMB => ${data.downloadData.totalDownloadSizeInMB.toFixed(
                  2
                )}`
              );
            }}
            autoFullscreenMode
            enableControls
          />
          <View>
            <Button
              title="PLAY"
              color="red"
              onPress={() => this.player.play()}
            />
            <Button
              title="PAUSE"
              color="green"
              onPress={() => this.player.pause()}
            />
            <Button
              title="STOP"
              color="black"
              onPress={() => this.player.stop()}
            />
            <Button
              title="DOWNLOAD"
              color="blue"
              onPress={() => this.player.prepareDownload()}
            />
          </View>
          {/* 
<SambaPlayer style={styles.overLay}
              mediaIdOrLiveChannelId="915a8b06358fc57f0a37feeed3dad854"
              projectHash="242eea8683ff04266288d743be6d9eac"
              accessToken="01a911bd-4fc6-4a30-a455-fd5644f3fa77"
              styles={styles.overLay}
              OnPlay={ (event) => console.log('OnPlay 2 => ' + JSON.stringify(event))}
              autoFullscreenMode={false}
              enableControls={false}
             />

<SambaPlayer style={styles.overLay}
              mediaIdOrLiveChannelId="7eeecf40723fd2e3005c696afd6b480a"
              projectHash="242eea8683ff04266288d743be6d9eac"
              accessToken="01a911bd-4fc6-4a30-a455-fd5644f3fa77"
              styles={styles.overLay}
              OnPlay={ (event) => console.log('OnPlay 3 => ' + JSON.stringify(event))}
             /> */}
        </View>
      </>
    );
  };
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
  },
  layer1: {
    flex: 1,
    backgroundColor: 'lightgreen',
  },
  overLay: {
    // height:'25%',
    // width:'100%',
    backgroundColor: 'lightblue',
    flex: 1,
    // position: 'absolute',
    // top:'50%',
  },
  centeredView: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 22,
    backgroundColor: '#cccccc',
  },
  modalView: {
    margin: 20,
    backgroundColor: 'white',
    borderRadius: 20,
    padding: 35,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 5,
  },
  openButton: {
    backgroundColor: '#F194FF',
    borderRadius: 20,
    padding: 10,
    elevation: 2,
  },
  textStyle: {
    color: 'white',
    fontWeight: 'bold',
    textAlign: 'center',
  },
  modalText: {
    marginBottom: 15,
    textAlign: 'center',
    color: 'red',
  },
  item: {
    margin: 1,
    width: '80%',
  },
});
