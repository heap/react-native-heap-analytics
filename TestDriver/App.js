import React, { Component } from "react";
import { Button, StyleSheet, View } from "react-native";

import Heap from "react-native-heap-analytics";

export default class App extends Component {
  componentDidMount() {
    Heap.setAppId("2084764307");
  }

  render() {
    return (
      <View style={styles.container}>
        <Button title="Send Manual Event" onPress={this.sendEvent} />
      </View>
    );
  }

  sendEvent = () => {
    Heap.track("manual_button_pressed", { foo: "bar" });
  };
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#F5FCFF"
  }
});