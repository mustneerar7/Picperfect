import React from 'react';
import { View, Text, TouchableOpacity, ScrollView } from 'react-native';


const CorpControls = ({ crop, setCrop }) => {
    return (
        <ScrollView
            horizontal
            showsHorizontalScrollIndicator={false}
            style={{
                flexDirection: 'row',
                width: '90%',
                marginTop: 16,
            }}
            contentContainerStyle={{
                justifyContent: 'space-between',
                paddingLeft: 8,
                paddingRight: 8,
            }}
        >
            <TouchableOpacity
                style={{
                    width: 42,
                    height: 42,
                    borderRadius: 4,
                    backgroundColor: "#2b2b2b",
                    alignItems: "center",
                    justifyContent: "center",
                    marginLeft: 8,
                    marginRight: 8,
                }}
                onPress={() => {
                    setCrop(1);
                }}
            >
                <Text style={{ color: 'white' }}>1:1</Text>
            </TouchableOpacity>
            <TouchableOpacity
                style={{
                    width: 42,
                    height: 52,
                    borderRadius: 4,
                    backgroundColor: "#2b2b2b",
                    alignItems: "center",
                    justifyContent: "center",
                    marginLeft: 8,
                    marginRight: 8,
                }}
                onPress={() => {
                    setCrop(0.75);
                }}
            >
                <Text style={{ color: 'white' }}>3:4</Text>
            </TouchableOpacity>
            <TouchableOpacity
                style={{
                    width: 50,
                    height: 42,
                    borderRadius: 4,
                    backgroundColor: "#2b2b2b",
                    alignItems: "center",
                    justifyContent: "center",
                    marginLeft: 8,
                    marginRight: 8,
                }}
                onPress={() => {
                    setCrop(1.33);
                }}
            >
                <Text style={{ color: 'white' }}>4:3</Text>
            </TouchableOpacity>
            <TouchableOpacity
                style={{
                    width: 60,
                    height: 40,
                    borderRadius: 4,
                    backgroundColor: "#2b2b2b",
                    alignItems: "center",
                    justifyContent: "center",
                    marginLeft: 8,
                    marginRight: 8,
                }}
                onPress={() => {
                    setCrop(1.77);
                }}
            >
                <Text style={{ color: 'white' }}>16:9</Text>
            </TouchableOpacity>

            <TouchableOpacity
                style={{
                    width: 68,
                    height: 42,
                    borderRadius: 4,
                    backgroundColor: "#2b2b2b",
                    alignItems: "center",
                    justifyContent: "center",
                    marginLeft: 8,
                    marginRight: 8,
                }}
                onPress={() => {
                    setCrop(1.5);
                }}
            >
                <Text style={{ color: 'white' }}>3:2</Text>
            </TouchableOpacity>
        </ScrollView>
    );
};

export { CorpControls };