<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" isELIgnored="false"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Google Chart in JSP-Servlet</title>
    <script
            src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

    <script type="text/javascript"
            src="https://www.gstatic.com/charts/loader.js"></script>

    <script type="text/javascript">


        function searchNews() {
           var newsId= $("#newsId").val();
            $(document).ready(function() {
                $.ajax({
                    type : 'GET',
                    headers : {
                        Accept : "application/json; charset=utf-8",
                        "Content-Type" : "application/json; charset=utf-8"
                    },
                    url : '${pageContext.request.contextPath}/keyword/topten/'+newsId,
                    success : function(result) {
                        google.charts.load('current', {
                            'packages' : [ 'corechart' ]
                        });
                        google.charts.setOnLoadCallback(function() {
                            drawChart(result);
                        });
                        drawNLPChart();
                        getNewsTitle();
                        getWordCloud();
                    }
                });

                function drawChart(result) {
                    var data = new google.visualization.DataTable();
                    data.addColumn('string', 'Term');
                    data.addColumn('number', 'Term Frequency');
                    var dataArray = [];
                    $.each(result, function(i, obj) {
                        dataArray.push([ obj.stem, obj.frequency ]);
                    });

                    data.addRows(dataArray);


                    var barchart_options = {
                        title : 'Barchart: Top-10 Keywords in this News',
                        width : 600,
                        height : 400,
                        legend : 'none',
                        hAxis: {
                            title: 'Term Frequency',
                            minValue: 0
                        },
                        vAxis: {
                            title: 'Term'
                        }
                    };
                    var barchart = new google.visualization.BarChart(document
                        .getElementById('barchart_div'));
                    barchart.draw(data, barchart_options);
                }



                function getNewsTitle() {
                    $(document).ready(function() {
                        $.ajax({
                            type : 'GET',
                            headers : {
                                Accept : "application/json; charset=utf-8",
                                "Content-Type" : "application/json; charset=utf-8"
                            },
                            url : '${pageContext.request.contextPath}/news/'+newsId,
                            success :function(response) {
                                $('#newsTitle').html(response.title);
                            }
                        });
                    });
                }

                function getWordCloud() {
                    $(document).ready(function() {
                        $.ajax({
                            type : 'GET',
                            headers : {
                                Accept : "text/plain;charset=UTF-8",
                                "Content-Type" : "text/plain;charset=UTF-8"
                            },
                            url : '${pageContext.request.contextPath}/keyword/wordcloud/'+newsId,
                            success :function(data) {
                                $('#wordcloud').attr("src","data:image/png;base64,"+data);
                            }
                        });
                    });
                }

                function drawNLPChart(){
                    $(document).ready(function() {
                        $.ajax({
                            type : 'GET',
                            headers : {
                                Accept : "application/json; charset=utf-8",
                                "Content-Type" : "application/json; charset=utf-8"
                            },
                            url : '${pageContext.request.contextPath}/keyword/nlp/'+newsId,
                            success : function(result) {
                                google.charts.load('current', {
                                    'packages' : [ 'corechart' ]
                                });
                                google.charts.setOnLoadCallback(function() {
                                    drawChart(result);
                                });

                            }
                        });

                        function drawChart(result) {
                            var data = new google.visualization.DataTable();
                            data.addColumn('string', 'Lemma');
                            data.addColumn('number', 'TF-IDF');
                            var dataArray = [];
                            $.each(result, function(i, obj) {
                                dataArray.push([ obj.lemma, obj.tfIdf ]);
                            });

                            data.addRows(dataArray);


                            var barchart_options = {
                                title : 'Barchart: Top-10 NLP Keywords in this News',
                                width : 600,
                                height : 400,
                                legend : 'none',
                                hAxis: {
                                    title: 'TF-IDF',
                                    minValue: 0
                                },
                                vAxis: {
                                    title: 'Keywords Lemma'
                                }
                            };
                            var barchart = new google.visualization.BarChart(document
                                .getElementById('nlpbarchart_div'));
                            barchart.draw(data, barchart_options);
                        }

                    });

                }

                });
        }

        function startScrpe(){
            $(document).ready(function() {
                $.ajax({
                    type : 'GET',
                    headers : {
                        Accept : "application/json; charset=utf-8",
                        "Content-Type" : "application/json; charset=utf-8"
                    },
                    url : '${pageContext.request.contextPath}/news/bbc',
                    success :function(response) {
                        $('#statusMessage').html(response);
                        console.log("response=====",response);
                    }
                });
            });
        }

            function saveKeywords() {
                $(document).ready(function() {
                    $.ajax({
                        type : 'GET',
                        headers : {
                            Accept : "application/json; charset=utf-8",
                            "Content-Type" : "application/json; charset=utf-8"
                        },
                        url : '${pageContext.request.contextPath}/keyword/allkeywords',
                        success :function(response) {
                            $('#statusMessage').html(response);
                            console.log("keyword",response);
                        }
                    });
                });
            }

            function saveNLPKeywords() {
                $(document).ready(function() {
                    $.ajax({
                        type : 'GET',
                        headers : {
                            Accept : "application/json; charset=utf-8",
                            "Content-Type" : "application/json; charset=utf-8"
                        },
                        url : '${pageContext.request.contextPath}/keyword/nlp',
                        success :function(response) {
                            $('#statusMessage').html(response);
                            console.log("NLP",response);
                        }
                    });
                });
            }



    </script>

</head>
<br>
<button onclick="startScrpe()">Start Scrape</button>
<button onclick="saveKeywords()">Keywords</button>
<button onclick="saveNLPKeywords()">NLP Keywords</button>
<br><br>
<h3 id="statusMessage"></h3>
<label>Select a news:</label>
<input type="text" id="newsId" >
<button onclick="searchNews()">Search</button>
<br><br>
<h1 id="newsTitle"></h1>
<table class="columns">
    <tr style="border: 1px solid #ccc">

        <td><div id="barchart_div" style="border: 1px solid #ccc"></div></td>
        <td><div id="nlpbarchart_div" style="border: 1px solid #ccc"></div></td>

    </tr>
    <div style="border: 1px solid #ccc"><img id="wordcloud"></div>
</table>

</body>
</html>

