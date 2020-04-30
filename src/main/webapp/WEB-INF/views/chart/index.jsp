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
                    url : '${pageContext.request.contextPath}/keyword/'+newsId,
                    success : function(result) {
                        google.charts.load('current', {
                            'packages' : [ 'corechart' ]
                        });
                        google.charts.setOnLoadCallback(function() {
                            drawChart(result);
                        });
                        getNewsTitle();
                    }
                });

                function drawChart(result) {
                    var data = new google.visualization.DataTable();
                    data.addColumn('string', 'Term');
                    data.addColumn('number', 'TF-IDF');
                    var dataArray = [];
                    $.each(result, function(i, obj) {
                        dataArray.push([ obj.term, obj.tfidf ]);
                    });

                    data.addRows(dataArray);

                    var piechart_options = {
                        title : 'Pie Chart: key Words in this News',
                        width : 500,
                        height : 400
                    };
                    var piechart = new google.visualization.PieChart(document
                        .getElementById('piechart_div'));
                    piechart.draw(data, piechart_options);

                }

                function getNewsTitle() {
                    $(document).ready(function() {
                        $.ajax({
                            type : 'GET',
                            headers : {
                                Accept : "application/json; charset=utf-8",
                                "Content-Type" : "application/json; charset=utf-8"
                            },
                            url : '${pageContext.request.contextPath}/news/bbc/'+newsId,
                            success :function(response) {
                                $('#newsTitle').html(response.title);
                            }
                        });
                    });
                }
            });
        }


    </script>

</head>
<body>
<label>Select a news:</label>
<input type="text" id="newsId" >
<button onclick="searchNews()">Search</button>
<br><br>
<h1 id="newsTitle"></h1>
<table class="columns">
    <tr>
        <td><div id="piechart_div" style="border: 1px solid #ccc"></div></td>
<%--        <td><div id="barchart_div" style="border: 1px solid #ccc"></div></td>--%>
    </tr>
</table>

</body>
</html>

