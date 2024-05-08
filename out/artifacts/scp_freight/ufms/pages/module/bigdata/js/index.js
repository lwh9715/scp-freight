/**
 * 自调用函数
 */
(function () {
    // 1、页面一加载就要知道页面宽度计算
    var setFont = function () {
        // 因为要定义变量可能和别的变量相互冲突，污染，所有用自调用函数
        var html = document.documentElement;// 获取html
        // 获取宽度
        var width = html.clientWidth;

        // 判断
        if (width < 1024) width = 1024
        if (width > 1920) width = 1920
        // 设置html的基准值
        var fontSize = width / 80 + 'px';
        // 设置给html
        html.style.fontSize = fontSize;
    }
    setFont();
    // 2、页面改变的时候也需要设置
    // 尺寸改变事件
    window.onresize = function () {
        setFont();
    }
})();

(function () {
    //事件委托
    $('.monitor').on('click', ' a', function () {
        //点击当前的a 加类名 active  他的兄弟删除类名
        $(this).addClass('active').siblings().removeClass('active');
        //获取一一对应的下标
        var index = $(this).index();
        //选取content 然后狗日对应下标的 显示   当前的兄弟.content隐藏
        $('.content').eq(index).show().siblings('.content').hide();
    });
    //滚动
    //原理：把marquee下面的子盒子都复制一遍 加入到marquee中
    //      然后动画向上滚动，滚动到一半重新开始滚动
    //因为选取的是两个marquee  所以要遍历
    $('.monitor .marquee').each(function (index, dom) {
        //将每个 的所有子级都复制一遍
        var rows = $(dom).children().clone();
        //再将新的到的加入原来的
        $(dom).append(rows);
    });

})();

/**
 * T量(年,季,月,周)
 * @param {*} data
 */
function init_left_data1(data) {

    document.querySelector("#yearcnts").innerText = data.year;
    document.querySelector("#quarcnts").innerText = data.quarter;
    document.querySelector("#monthcnts").innerText = data.month;
    document.querySelector("#weekcnts").innerText = data.week;

}

/**
 * 今天订舱量_总部部门和分公司
 * @param {*} data
 */
function init_left_data2(bookingCompany, bookingCompanyValue) {
    var myechart = echarts.init($('.booking')[0]);

    option = {
        // 工具提示
        tooltip: {
            // 触发类型  经过轴触发axis  经过轴触发item
            trigger: 'item',
            // 轴触发提示才有效
            axisPointer: {
                // 默认为直线，可选为：'line' 线效果 | 'shadow' 阴影效果
                type: 'shadow'
            }
        },
        grid: {
            // 距离 上右下左 的距离
            left: '0',
            right: '3%',
            bottom: '3%',
            top: '5%',
            // 大小是否包含文本【类似于boxsizing】
            containLabel: true,
            //显示边框
            show: true,
            //边框颜色
            borderColor: 'rgba(0, 240, 255, 0.3)'
        },
        xAxis: {
            type: 'category',
            data: ['业务2部', '中集世联达上海'],
            axisTick: {
                alignWithLabel: false,
                show: false
            },
            axisLabel: {
                fontSize: 10,
                color: '#4c9bfd',
                interval: 0,
                rotate: 50
            }
        },
        yAxis: {
            type: 'value',
            axisTick: {
                alignWithLabel: false,
                show: false
            },
            axisLabel: {
                color: '#4c9bfd'
            },
            splitLine: {
                lineStyle: {
                    color: 'rgba(0, 240, 255, 0.3)'
                }
            },
        },
        series: [
            {
                type: 'bar',
                data: [0, 58],
                label: {
                    show: true,
                    color: 'rgba(255,255,255,1)'
                },
                axisLabel: {
                    fontSize: 8,
                },
                itemStyle: {
                    color: new echarts.graphic.LinearGradient(
                        0, 0, 0, 1,
                        [
                            {offset: 0, color: '#00fffb'},
                            {offset: 1, color: '#0061ce'}
                        ]
                    )
                },
            }
        ]
    };
    option.xAxis.data = bookingCompany;
    option.series[0].data = bookingCompanyValue;

    myechart.setOption(option);
}


/**
 * 航线订舱量_航线
 * @param {*} data
 */
function init_left_data2_1(data) {
    var myechart = echarts.init($('.booking1')[0]);

    option = {
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        series: [
            {
                name: '今天订舱量',
                type: 'pie',
                radius: ['0%', '60%'],
                center: ['50%', '50%'],
                data: [
                    {value: 23, name: '中东线'},
                    {value: 5, name: '中国'},
                    {value: 12, name: '印巴线'},
                    {value: 32, name: '欧洲线'},
                    {value: 19, name: '东南亚'},
                    {value: 23, name: '澳洲线'},
                    {value: 3, name: '红海线'},
                    {value: 14, name: '美国西'},
                    {value: 52, name: '墨西哥'},
                    {value: 22, name: '南美西线'},
                    {value: 12, name: '其他'}
                ],
                minAngle: 5,
                label: {
                    fontSize: 10
                },
                labelLine: {
                    length: 8,
                    length2: 10
                }
            }
        ],
        color: ['#006cff', '#60cda0', '#ed8884', '#ff9f7f', '#0096ff', '#9fe6b8', '#32c5e9', '#1d9dff']
    };
    option.series[0].data = data;
    myechart.setOption(option);
}


/**
 * 航线订舱量_航线
 * @param {*} data
 */
function init_left_data2_2(data) {
    document.querySelector("#countcnts").innerText = "今天订仓量：" + data[0].value;
}

/**
 * 航线T量分析
 * @param {*} data
 */
function init_left_data3(data) {
    var myechart = echarts.init($('.pie')[0]);
    option = {
        // 控制提示
        tooltip: {
            // 非轴图形，使用item的意思是放到数据对应图形上触发提示
            trigger: 'item',
            // 格式化提示内容：
            // a 代表图表名称 b 代表数据名称 c 代表数据  d代表  当前数据/总数据的比例
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            type: "scroll",
            orient: 'vertical',
            left: '10%',
            top: 'middle',
            data: [],
            textStyle: {
                fontSize: 10,
                color: ['#006cff', '#60cda0', '#ed8884', '#ff9f7f', '#0096ff', '#9fe6b8', '#32c5e9', '#1d9dff']
            }
        },
        // 控制图表
        series: [
            {
                // 图表名称
                name: '航线分析',
                // 图表类型
                type: 'pie',
                // 南丁格尔玫瑰图 有两个圆  内圆半径10%  外圆半径70%
                // 百分比基于  图表DOM容器的半径
                radius: ['0%', '70%'],
                // 图表中心位置 left 50%  top 50% 距离图表DOM容器
                center: ['70%', '50%'],
                // 半径模式，另外一种是 area 面积模式
                //                roseType: 'radius',
                minAngle: 5,
                // 数据集 value 数据的值 name 数据的名称
                data: [],
                //文字调整
                label: {
                    fontSize: 10
                },
                //引导线
                labelLine: {
                    length: 8,
                    length2: 10
                }
            }
        ],
        color: ['#006cff', '#60cda0', '#ed8884', '#ff9f7f', '#0096ff', '#9fe6b8', '#32c5e9', '#1d9dff']
    };
    option.series[0].data = data;
    var legenddata = new Array();
    for (var int = 0; int < data.length; int++) {
        legenddata[int] = data[int].name;
    }
    option.legend.data = legenddata;
    myechart.setOption(option);
}

/**
 * 航线地图
 * @param {*} map
 * @param {*} geoCoordMap
 * @param {*} tvalue
 */
function init_inner_data1(map, geoCoordMap, tvalue) {

    var planePath = 'path://M.6,1318.313v-89.254l-319.9-221.799l0.073-208.063c0.521-84.662-26.629-121.796-63.961-121.491c-37.332-0.305-64.482,36.829-63.961,121.491l0.073,208.063l-319.9,221.799v89.254l330.343-157.288l12.238,241.308l-134.449,92.931l0.531,42.034l175.125-42.917l175.125,42.917l0.531-42.034l-134.449-92.931l12.238-241.308L1705';
    var name = new Array();
    for (var i = 0; i < map.length; i++) {
        if ("深圳" != map[i][0][0].name) {//暂时只展示深圳数据
            continue;
        }
        name[i] = new Array();
        name[i][0] = map[i][0][0].name;
        name[i][1] = map[i];
    }
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

    var color = ['#3ed4ff', '#006cff', '#60cda0', '#ed8884', '#ff9f7f', '#0096ff', '#9fe6b8', '#32c5e9', '#1d9dff'];
    var series = [];
    name.forEach(function (item, i) {
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
                    color: '#3ed4ff',
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
                    color: '#3ed4ff',
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
                    formatter: function (params, ticket, callback) {
                        //根据业务自己拓展要显示的内容
                        var res = "";
                        var name = params.name;
                        return name;
                    }
                }
            },
            symbolSize: function (val) {
                return val[2] / 8;
            },
            itemStyle: {
                normal: {
                    color: '#3ed4ff'
                }
            },
            data: item[1].map(function (dataItem) {
                return {

                    name: dataItem[1].name,
                    value: geoCoordMap[dataItem[1].name].concat((tvalue[dataItem[1].name] / 50 + 30) > 120 ? 120 : (tvalue[dataItem[1].name] / 50 + 30))
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
            trigger: 'item',
            formatter: function (params, ticket, callback) {
                //根据业务自己拓展要显示的内容
                var res = "";
                var name = params.name;
                var value = tvalue[name];
                if (null == name || "" == name) {
                    name = "FROM " + params.seriesName;
                    res = "<span style='color:#fff;'>" + name + "</span>";
                } else if (null == value || "" == value) {
                    res = "<span style='color:#fff;'>" + name + "</span>";
                } else {
                    res = "<span style='color:#fff;'>" + name + "</span><br/>T量：" + value;
                }
                return res;
            }
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
    myecharts.clear();
    option.series = series;
    myecharts.setOption(option);
}

/**
 * 船公司T量分析
 * @param {*} shipper
 * @param {*} shipvalue
 */
function init_inner_data2(shipper, shipvalue) {
    // 中间省略的数据  准备三项
    var item = {
        name: '',
        value: 1200,
        // 柱子颜色
        itemStyle: {
            color: '#254065'
        },
        // 鼠标经过柱子颜色
        emphasis: {
            itemStyle: {
                color: '#254065'
            }
        },
        // 工具提示隐藏
        tooltip: {
            extraCssText: 'opacity:0'
        }
    };
    option = {
        // 工具提示
        tooltip: {
            // 触发类型  经过轴触发axis  经过轴触发item
            trigger: 'item',
            // 轴触发提示才有效
            axisPointer: {
                // 默认为直线，可选为：'line' 线效果 | 'shadow' 阴影效果
                type: 'shadow'
            }
        },
        // 图表边界控制
        grid: {
            // 距离 上右下左 的距离
            left: '0',
            right: '3%',
            bottom: '3%',
            top: '5%',
            // 大小是否包含文本【类似于boxsizing】
            containLabel: true,
            //显示边框
            show: true,
            //边框颜色
            borderColor: 'rgba(0, 240, 255, 0.3)'
        },
        // 控制x轴
        xAxis: [
            {
                // 使用类目，必须有data属性
                type: 'category',
                // 使用 data 中的数据设为刻度文字
                data: [],
                // 刻度设置
                axisTick: {
                    // true意思：图形在刻度中间
                    // false意思：图形在刻度之间
                    alignWithLabel: false,
                    show: false
                },
                //文字
                axisLabel: {
                    fontSize: 10,
                    color: '#4c9bfd',
                    interval: 0
                }
            }
        ],
        // 控制y轴
        yAxis: [
            {
                // 使用数据的值设为刻度文字
                type: 'value',
                axisTick: {
                    // true意思：图形在刻度中间
                    // false意思：图形在刻度之间
                    alignWithLabel: false,
                    show: false
                },
                //文字
                axisLabel: {
                    color: '#4c9bfd'
                },
                splitLine: {
                    lineStyle: {
                        color: 'rgba(0, 240, 255, 0.3)'
                    }
                },
            }
        ],
        // 控制x轴
        series: [

            {
                // series配置
                // 颜色
                itemStyle: {
                    // 提供的工具函数生成渐变颜色
                    color: new echarts.graphic.LinearGradient(
                        // (x1,y2) 点到点 (x2,y2) 之间进行渐变
                        0, 0, 0, 1,
                        [
                            {offset: 0, color: '#00fffb'}, // 0 起始颜色
                            {offset: 1, color: '#0061ce'}  // 1 结束颜色
                        ]
                    )
                },
                // 图表数据名称
                name: '船公司T量',
                // 图表类型
                type: 'bar',
                // 柱子宽度
                barWidth: '60%',
                // 数据
                data: [2100, 1900, 1700, 1560, 1400, 900, 750, 600, 480, 240]
            }
        ]
    };
    var myechart = echarts.init($('.users .bar')[0]);
    option.xAxis[0].data = shipper;
    option.series[0].data = shipvalue;
    myechart.setOption(option);
}

//初始化页面
index_init();

function index_init() {
    layui.use('layer', function () {
        var layer = layui.layer;
        var loading = layer.load(0, {
            shade: false,
            time: 3 * 1000
        });
        var anayear = $("#anayear").val();
        var companyid = $("#company").val();
        var company = $("#company option:selected").text();
        if ("0" == companyid) {
            document.querySelector("#compordept").innerText = "分公司分析";
        } else {
            document.querySelector("#compordept").innerText = company + "部门分析";
        }
        $(".viewport").css("background", "url(../bigdata/images/ufms.png) no-repeat 0 0 / contain");

        actionGet('/analyse_index', 'get_sys_config', '', function (res) {
            if (null != res && "" != res && (res == '8888' || res == '2274')) {
                $(".viewport").css("background", "url(../bigdata/images/gsit.png) no-repeat 0 0 / contain");//根据服务号判断使用LOGO
            }
        });
        actionAsyncGet('/analyse_index', 'get_index_data', 'anayear=' + anayear + '&companyid=' + companyid, function (res) {
            var data = JSON.parse(res);
            if (null != data && '' != data) {


                var leftdata1 = data.leftdata1;//
                var company = data.company;//分公司值
                var bookingToday = data.booking.bookingToday;//今日订舱量总公司部门/分公司
                var bookingCompany = data.booking.Company;//今日订舱量总公司部门/分公司
                var bookingCompanyValue = data.booking.CompanyValue;//今日订舱量总公司部门/分公司
                var bookingLine = data.booking.bookingLine;//今日订舱量航线
                var route = data.route;//航线值
                var shipper = data.shipper;//船公司名称
                var shipvalue = data.shipvalue;//船公司对应值
                compayear1 = data.compayear1;//比较年1
                compayear2 = data.compayear2;//比较年2
                rightdata1 = data.rightdata1;
                rightdata2 = data.rightdata2;
                var bycust = data.rightdata3;
                var compordept = data.rightdata4;
                var iscomp = data.iscomp;
                var map = data.map;
                var geoCoordMap = data.geoCoordMap;
                var tvalue = data.tvalue;

                init_select(company);
                $("#anayear").val(anayear);
                $("#company").val(companyid);
                init_left_data1(leftdata1);
                init_left_data2(bookingCompany, bookingCompanyValue);
                init_left_data2_1(bookingLine);
                init_left_data2_2(bookingToday);
                init_left_data3(route);
                if (null != map && null != geoCoordMap && map.length > 0) {
                    init_inner_data1(map, geoCoordMap, tvalue);
                }
                init_inner_data2(shipper, shipvalue);
                init_right_data1(rightdata1);
                init_right_data2();
                init_right_data3(bycust);
                init_right_data4(compordept, iscomp);

                layer.close(loading);
            }
        });
    });
}

/**
 * 所有分公司
 * @param {*} company
 * @returns
 */
function init_select(company) {
    if ('' == company || null == company) {
        return false;
    }
    var company_option = '<option value="0">所有分公司</option>';
    $('#company').html("");
    for (var i = 0; i < company.length; i++) {
        var value = company[i].id;
        var abbr = company[i].abbr;
        company_option += '<option value="' + value + '">' + abbr + '</option>';
    }
    $('#company').html(company_option);
}

/**
 * T量对比-1
 */
var rightdata1 = '';

function init_right_data1(data) {
    $('.order .filter a').eq(0).click();
    $('.order .item h4:eq(0)').text(data.day365.cnts1);
    $('.order .item h4:eq(1)').text(data.day365.cnts2);
    //点击事件
    var index = 0;
    $('.order').on('click', '.filter a', function () {
        //点击之后加类名
        $(this).addClass('active').siblings().removeClass('active');
        // 先获取点击a的 data-key自定义属性
        var key = $(this).attr('data-key');
        key = data[key];
        $('.order .item h4:eq(0)').text(key.cnts1);
        $('.order .item h4:eq(1)').text(key.cnts2);
    });
}

/**
 * T量对比-2
 */
var rightdata2 = '';
var compayear1 = '';
var compayear2 = '';

function init_right_data2() {
    var optionyear = {
        //鼠标提示工具
        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            // 类目类型
            type: 'category',
            // x轴刻度文字
            data: [compayear1, compayear2],
            axisTick: {
                show: false//去除刻度线
            },
            axisLabel: {
                color: '#4c9bfd'//文本颜色
            },
            axisLine: {
                show: false//去除轴线
            },
            boundaryGap: false//去除轴内间距
        },
        yAxis: {
            // 数据作为刻度文字
            type: 'value',
            axisTick: {
                show: false//去除刻度线
            },
            axisLabel: {
                color: '#4c9bfd'//文本颜色
            },
            axisLine: {
                show: false//去除轴线
            },
            boundaryGap: false//去除轴内间距
        },
        //图例组件
        legend: {
            textStyle: {
                color: '#4c9bfd' // 图例文字颜色

            },
            right: '10%'//距离右边10%
        },
        // 设置网格样式
        grid: {
            show: true,// 显示边框
            top: '20%',
            left: '3%',
            right: '4%',
            bottom: '3%',
            borderColor: '#012f4a',// 边框颜色
            containLabel: true // 包含刻度文字在内
        },
        series: [{
            name: '年T量对比',
            // 数据
            data: [24, 40],
            // 图表类型
            type: 'line',
            // 圆滑连接
            smooth: true,
            itemStyle: {
                color: '#ed3f35'  // 线颜色
            }
        }]
    };
    var optionquarter = {
        //鼠标提示工具
        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            // 类目类型
            type: 'category',
            // x轴刻度文字
            data: ['Q1', 'Q2', 'Q3', 'Q4'],
            axisTick: {
                show: false//去除刻度线
            },
            axisLabel: {
                color: '#4c9bfd'//文本颜色
            },
            axisLine: {
                show: false//去除轴线
            },
            boundaryGap: false//去除轴内间距
        },
        yAxis: {
            // 数据作为刻度文字
            type: 'value',
            axisTick: {
                show: false//去除刻度线
            },
            axisLabel: {
                color: '#4c9bfd'//文本颜色
            },
            axisLine: {
                show: false//去除轴线
            },
            boundaryGap: false//去除轴内间距
        },
        //图例组件
        legend: {
            textStyle: {
                color: '#4c9bfd' // 图例文字颜色

            },
            right: '10%'//距离右边10%
        },
        // 设置网格样式
        grid: {
            show: true,// 显示边框
            top: '20%',
            left: '3%',
            right: '4%',
            bottom: '3%',
            borderColor: '#012f4a',// 边框颜色
            containLabel: true // 包含刻度文字在内
        },
        series: [{
            name: compayear1 + '季度T量',
            // 数据
            data: [21, 41, 31, 11],
            // 图表类型
            type: 'line',
            // 圆滑连接
            smooth: true,
            itemStyle: {
                color: '#00f2f1'  // 线颜色
            }
        }, {
            name: compayear2 + '季度T量',
            // 数据
            data: [24, 40, 30, 33],
            // 图表类型
            type: 'line',
            // 圆滑连接
            smooth: true,
            itemStyle: {
                color: '#ed3f35'  // 线颜色
            }
        }]
    };
    var optionmonth = {
        //鼠标提示工具
        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            // 类目类型
            type: 'category',
            // x轴刻度文字
            data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
            axisTick: {
                show: false//去除刻度线
            },
            axisLabel: {
                color: '#4c9bfd'//文本颜色
            },
            axisLine: {
                show: false//去除轴线
            },
            boundaryGap: false//去除轴内间距
        },
        yAxis: {
            // 数据作为刻度文字
            type: 'value',
            axisTick: {
                show: false//去除刻度线
            },
            axisLabel: {
                color: '#4c9bfd'//文本颜色
            },
            axisLine: {
                show: false//去除轴线
            },
            boundaryGap: false//去除轴内间距
        },
        //图例组件
        legend: {
            textStyle: {
                color: '#4c9bfd' // 图例文字颜色

            },
            right: '10%'//距离右边10%
        },
        // 设置网格样式
        grid: {
            show: true,// 显示边框
            top: '20%',
            left: '3%',
            right: '4%',
            bottom: '3%',
            borderColor: '#012f4a',// 边框颜色
            containLabel: true // 包含刻度文字在内
        },
        series: [{
            name: compayear1 + '月T量',
            // 数据
            data: [24, 40, 101, 134, 90, 230, 210, 230, 120, 230, 210, 120],
            // 图表类型
            type: 'line',
            // 圆滑连接
            smooth: true,
            itemStyle: {
                color: '#00f2f1'  // 线颜色
            }
        },
            {
                name: compayear2 + '月T量',
                // 数据
                data: [40, 64, 191, 324, 290, 330, 310, 213, 180, 200, 180, 79],
                // 图表类型
                type: 'line',
                // 圆滑连接
                smooth: true,
                itemStyle: {
                    color: '#ed3f35'  // 线颜色
                }
            }]
    };
    var optionweek = {
        //鼠标提示工具
        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            // 类目类型
            type: 'category',
            // x轴刻度文字
            data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31', '32', '33', '34', '35', '36', '37', '38', '39', '40', '41', '42', '43', '44', '45', '46', '47', '48', '49', '50', '51', '52', '53', '54'],
            axisTick: {
                show: false//去除刻度线
            },
            axisLabel: {
                color: '#4c9bfd'//文本颜色
            },
            axisLine: {
                show: false//去除轴线
            },
            boundaryGap: false//去除轴内间距
        },
        yAxis: {
            // 数据作为刻度文字
            type: 'value',
            axisTick: {
                show: false//去除刻度线
            },
            axisLabel: {
                color: '#4c9bfd'//文本颜色
            },
            axisLine: {
                show: false//去除轴线
            },
            boundaryGap: false//去除轴内间距
        },
        //图例组件
        legend: {
            textStyle: {
                color: '#4c9bfd' // 图例文字颜色

            },
            right: '10%'//距离右边10%
        },
        // 设置网格样式
        grid: {
            show: true,// 显示边框
            top: '20%',
            left: '3%',
            right: '4%',
            bottom: '3%',
            borderColor: '#012f4a',// 边框颜色
            containLabel: true // 包含刻度文字在内
        },
        series: [{
            name: compayear1 + '周T量',
            // 数据
            data: [24, 40, 101, 134, 90, 230, 210, 230, 120, 230, 210, 120],
            // 图表类型
            type: 'line',
            // 圆滑连接
            smooth: true,
            itemStyle: {
                color: '#00f2f1'  // 线颜色
            }
        },
            {
                name: compayear2 + '周T量',
                // 数据
                data: [40, 64, 191, 324, 290, 330, 310, 213, 180, 200, 180, 79],
                // 图表类型
                type: 'line',
                // 圆滑连接
                smooth: true,
                itemStyle: {
                    color: '#ed3f35'  // 线颜色
                }
            }]
    };
    var data = rightdata2;
    optionmonth.series[0].data = data.month[0];
    optionmonth.series[1].data = data.month[1];
    var myechart = echarts.init($('.line')[0]);
    myechart.clear();
    $('.sales .caption a').eq(2).click();
    myechart.setOption(optionmonth);
    //	$('.line')[0].style.display='none';
    //点击效果
    var index = 2;
    $('.sales ').on('click', '.caption a', function () {
        $(this).addClass('active').siblings('a').removeClass('active');
        //option series   data
        //获取自定义属性值
        var key = $(this).attr('data-type');
        //取出对应的值
        key = data[key];

        var type = this.innerText;
        switch (type) {
            case '年':
                //将值设置到 图表中
                if (null == key || "" == key || "undefined" == key) {
                    myechart.clear();
                    break;
                }
                optionyear.series[0].data = [key[0], key[1]];
                //再次调用才能在页面显示
                myechart.clear();
                myechart.setOption(optionyear);
                index = 0;
                break;
            case '季':
                //将值设置到 图表中
                if (null == key || "" == key || "undefined" == key) {
                    myechart.clear();
                    break;
                }
                optionquarter.series[0].data = key[0];
                optionquarter.series[1].data = key[1];
                //再次调用才能在页面显示
                myechart.clear();
                myechart.setOption(optionquarter);
                index = 1;
                break;
            case '月':
                //将值设置到 图表中
                if (null == key || "" == key || "undefined" == key) {
                    myechart.clear();
                    break;
                }
                optionmonth.series[0].data = key[0];
                optionmonth.series[1].data = key[1];
                //再次调用才能在页面显示
                myechart.clear();
                myechart.setOption(optionmonth);
                index = 2;
                break;
            case '周':
                //将值设置到 图表中
                if (null == key || "" == key || "undefined" == key) {
                    myechart.clear();
                    break;
                }
                optionweek.series[0].data = key[0];
                optionweek.series[1].data = key[1];
                //再次调用才能在页面显示
                myechart.clear();
                myechart.setOption(optionweek);
                index = 3;
                break;
            default:
                index = 2;
                break;
        }
    });
}

/**
 * 客户类型分析
 * @param {*} data
 */
function init_right_data3(data) {
    var myechart = echarts.init($('.pie2')[0]);
    option = {
        // 控制提示
        tooltip: {
            // 非轴图形，使用item的意思是放到数据对应图形上触发提示
            trigger: 'item',
            // 格式化提示内容：
            // a 代表图表名称 b 代表数据名称 c 代表数据  d代表  当前数据/总数据的比例
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            type: "scroll",
            orient: 'vertical',
            left: '0%',
            //	        align:'right',
            top: 'middle',
            data: [],
            textStyle: {
                fontSize: 10,
                color: ['#006cff', '#60cda0', '#ed8884']
            }
            //        	height:150
        },
        // 控制图表
        series: [
            {
                // 图表名称
                name: 'T量',
                // 图表类型
                type: 'pie',
                // 南丁格尔玫瑰图 有两个圆  内圆半径10%  外圆半径70%
                // 百分比基于  图表DOM容器的半径
                radius: ['0%', '70%'],
                // 图表中心位置 left 50%  top 50% 距离图表DOM容器
                center: ['45%', '45%'],
                // 半径模式，另外一种是 area 面积模式
                //                roseType: 'radius',
                minAngle: 5,
                // 数据集 value 数据的值 name 数据的名称
                data: [],
                //文字调整
                label: {
                    fontSize: 10
                },
                //引导线
                labelLine: {
                    length: 8,
                    length2: 10
                }
            }
        ],
        color: ['#006cff', '#60cda0', '#ed8884', '#ff9f7f', '#0096ff', '#9fe6b8', '#32c5e9', '#1d9dff']
    };
    if (null == data || data == "") {
        option.series[0].data = [];
        option.legend.data = [];
    } else {
        option.series[0].data = data;
        var legenddata = new Array();
        for (var int = 0; int < data.length; int++) {
            legenddata[int] = data[int].name;
        }
        option.legend.data = legenddata;
    }
    myechart.setOption(option);
}

/**
 * 分公司分析
 * @param {*} data
 * @param {*} iscomp
 */
function init_right_data4(data, iscomp) {
    var myechart = echarts.init($('.pie2')[1]);
    option = {
        // 控制提示
        tooltip: {
            // 非轴图形，使用item的意思是放到数据对应图形上触发提示
            trigger: 'item',
            // 格式化提示内容：
            // a 代表图表名称 b 代表数据名称 c 代表数据  d代表  当前数据/总数据的比例
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            type: "scroll",
            orient: 'vertical',
            left: '0%',
            //	        align:'right',
            top: 'middle',
            data: [],
            textStyle: {
                fontSize: 10,
                color: ['#006cff', '#60cda0', '#ed8884']
            },
            height: 150
        },
        // 控制图表
        series: [
            {
                // 图表名称
                name: 'T量',
                // 图表类型
                type: 'pie',
                // 南丁格尔玫瑰图 有两个圆  内圆半径10%  外圆半径70%
                // 百分比基于  图表DOM容器的半径
                radius: ['0%', '70%'],
                // 图表中心位置 left 50%  top 50% 距离图表DOM容器
                center: ['50%', '53%'],
                // 半径模式，另外一种是 area 面积模式
                //                roseType: 'radius',
                //最小角度
                minAngle: 5,
                // 数据集 value 数据的值 name 数据的名称
                data: [],
                //文字调整
                label: {
                    fontSize: 10
                },
                //引导线
                labelLine: {
                    length: 8,
                    length2: 10
                }
            }
        ],
        color: ['#006cff', '#60cda0', '#ed8884', '#ff9f7f', '#0096ff', '#9fe6b8', '#32c5e9', '#1d9dff']
    };
    var legenddata = new Array();
    if (null == data || data == "") {
        data = [];
        legenddata = [];
    }
    if (!iscomp) {
        $("#compordept").val("分公司部门分析");
    }
    for (var int = 0; int < data.length; int++) {
        legenddata[int] = data[int].name;
    }
    option.series[0].data = data;
    option.legend.data = legenddata;
    myechart.setOption(option);
}
