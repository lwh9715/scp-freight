var geoCoordMap = {
	'美国蒙特里': [-121.874729,36.541478 ],
	'荷兰奥特芬': [5.057415,52.735095 ],
	'上海': [121.4648, 31.2891],
	'中国长沙': [113.0823, 28.2568],
	'中国长治': [112.8625, 36.4746],
	'中国阳泉': [113.4778, 38.0951],
	'中国青岛': [120.4651, 36.3373],
	'中国韶关': [113.7964, 24.7028],
	'深圳': [114.24343015510262,22.563437914836104],
	'澳洲': [151.068595,-33.880926],
	'迪拜': [55.267149,25.202845],
	'宁波': [121.62857249434141,29.866033045866054]
}
var BJData = [
    [{
        name: '深圳'
    },{
		name: '深圳',
		value: (111-31)/62*50*0.5+50
	}],
    [{
        name: '深圳'
    }, {
        name: '澳洲',
        value: 30
    }],
    [{
        name: '深圳'
    }, {
        name: '迪拜',
        value: 30
    }]
];
var GZData = [
    [{
        name: '宁波'
    },{
  		name: '宁波',
  		value: 50
    }],
    [{
        name: '宁波'
    }, {
        name: '澳洲',
        value: 30
    }],
    [{
        name: '宁波'
    }, {
        name: '迪拜',
        value: 30
    }]
];
var planePath = 'path://M.6,1318.313v-89.254l-319.9-221.799l0.073-208.063c0.521-84.662-26.629-121.796-63.961-121.491c-37.332-0.305-64.482,36.829-63.961,121.491l0.073,208.063l-319.9,221.799v89.254l330.343-157.288l12.238,241.308l-134.449,92.931l0.531,42.034l175.125-42.917l175.125,42.917l0.531-42.034l-134.449-92.931l12.238-241.308L1705';

var convertData = function (data) {
    var res = [];
    for (var i = 0; i < data.length; i++) {
        var dataItem = data[i];
        var fromCoord = geoCoordMap[dataItem[0].name];
        var toCoord = geoCoordMap[dataItem[1].name];
        if (fromCoord && toCoord) {
            res.push([{
                coord: fromCoord
            }, {
                coord: toCoord
            }]);
        }
    }
    return res;
};

var color = ['#3ed4ff','#3ed4ff', '#ffa022', '#a6c84c'];
var series = [];
[
    ['深圳', BJData],
    ['宁波', GZData]
].forEach(function (item, i) {
    series.push({
        name: '1111',
        type: 'lines',
        zlevel: 1,
        effect: {
            show: true,
            period: 6,
            trailLength: 0.7,
            color: '#fff',
            symbolSize: 3
        },
        lineStyle: {
            normal: {
                color: color[i],
                width: 0,
                curveness: 0.2
            }
        },
        data: convertData(item[1])
    }, {
        name: item[0],
        type: 'lines',
        zlevel: 2,
        effect: {
            show: true,
            period: 6,
            trailLength: 0,
            symbol: planePath,
            symbolSize: 15
        },
        lineStyle: {
            normal: {
                color: color[i],
                width: 1,
                opacity: 0.4,
                curveness: 0.2
            }
        },
        data: convertData(item[1])
    }, {
        name: "",
        type: 'effectScatter',
        coordinateSystem: 'geo',
        zlevel: 2,
        rippleEffect: {
            brushType: 'stroke'
        },
        label: {
            normal: {
                show: true,
                position: 'right',
                formatter: '{b}'
            }
        },
        symbolSize: function (val) {
            return val[2] / 8;
        },
        itemStyle: {
            normal: {
                color: color[i]
            }
        },
        data: item[1].map(function (dataItem) {
            return {
                name: dataItem[1].name,
                value: geoCoordMap[dataItem[1].name].concat([dataItem[1].value])
            };
        })
    });
});

option = {
    backgroundColor: '#080a20',
    title: {
        left: 'left',
        textStyle: {
            color: '#fff'
        }
    },
    tooltip: {
        trigger: 'item'
    },
    legend: {
        orient: 'vertical',
        top: 'bottom',
        left: 'right',
        data: ['北京 Top10', '上海 Top10', '广州 Top10'],
        textStyle: {
            color: '#fff'
        },
        selectedMode: 'single'
    },
    geo: {
        map: 'world',
        zoom: 1.2,
        label: {
            emphasis: {
                show: false
            }
        },
        roam: true,
        itemStyle: {
            normal: {
                areaColor: '#142957',
                borderColor: '#0692a4'
            },
            emphasis: {
                areaColor: '#0b1c2d'
            }
        }
    },
    series: series
};
var myecharts = echarts.init($('.map .geo')[0]);
myecharts.setOption(option);