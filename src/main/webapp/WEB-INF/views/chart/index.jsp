<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" isELIgnored="false"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Google Chart in JSP-Servlet</title>
    <!-- Compiled and minified CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">

    <!-- Compiled and minified JavaScript -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>

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
                        Accept : "text/plain;charset=UTF-8",
                        "Content-Type" : "text/plain;charset=UTF-8"
                    },
                    url : '${pageContext.request.contextPath}/news/bbc',
                    beforeSend:beforeSend,
                    complete:complete,
                    success :function(response) {
                        $('#scrapeMessage').html(response);
                    }
                });
            });
        }

        function saveKeywords() {
                $(document).ready(function() {
                    $.ajax({
                        type : 'GET',
                        headers : {
                            Accept : "text/plain;charset=UTF-8",
                            "Content-Type" : "text/plain;charset=UTF-8"
                        },
                        url : '${pageContext.request.contextPath}/keyword/allkeywords',
                        beforeSend:beforeSend,
                        complete:complete,
                        success :function(response) {
                            $('#keywordMessage').html(response);
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
                            Accept : "text/plain;charset=UTF-8",
                            "Content-Type" : "text/plain;charset=UTF-8"
                        },
                        url : '${pageContext.request.contextPath}/keyword/nlp',
                        beforeSend:beforeSend,
                        complete:complete,
                        success :function(response) {
                            $('#NLPMessage').html(response);
                        }
                    });
                });
            }
        function beforeSend(XMLHttpRequest){
            $("#showResult").append("<div><img src='https://media.giphy.com/media/52qtwCtj9OLTi/giphy.gif' width='200'/><div>");
        }
        function complete(XMLHttpRequest, textStatus){
            $("#showResult").html("");
        }



    </script>

</head>
<body>
<div class="container">


<a class="waves-effect waves-light btn" onclick="startScrpe()">Start Scrape</a>
<a class="waves-effect waves-light btn" onclick="saveKeywords()">Keywords</a>
<a class="waves-effect waves-light btn" onclick="saveNLPKeywords()">NLP Keywords</a>

    <div id="showResult"></div>
    <ul class="collection">
<li class="collection-item" id="scrapeMessage" style="color: #0088CC"></li>
<li class="collection-item" id="keywordMessage" style="color: #0088CC"></li>
<li class="collection-item" id="NLPMessage" style="color: #0088CC"></li>
    </ul>

<h7>Select a news:</h7>
<input type="text" id="newsId" >
    <a class="waves-effect waves-light btn" onclick="searchNews()">Search</a>


<h4 id="newsTitle"></h4>
<table class="highlight">
    <tr>

        <td><div id="barchart_div" ></div></td>
        <td><div id="nlpbarchart_div" ></div></td>

    </tr>
    <div ><img id="wordcloud"></div>
</table>
</div>
</body>
</html>

