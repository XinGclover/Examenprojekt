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
        $(document).ready(function() {
            $.ajax({
                type : 'GET',
                headers : {
                    Accept : "application/json; charset=utf-8",
                    "Content-Type" : "application/json; charset=utf-8"
                },
                url : '${pageContext.request.contextPath}/keyword/2',
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

            //     var barchart_options = {
            //         title : 'Barchart: How Much Products Sold By Last Night',
            //         width : 400,
            //         height : 300,
            //         legend : 'none'
            //     };
            //     var barchart = new google.visualization.BarChart(document
            //         .getElementById('barchart_div'));
            //     barchart.draw(data, barchart_options);
               }

        });
    </script>

</head>
<body>
<h1></h1>
<table class="columns">
    <tr>
        <td><div id="piechart_div" style="border: 1px solid #ccc"></div></td>
<%--        <td><div id="barchart_div" style="border: 1px solid #ccc"></div></td>--%>
    </tr>
</table>

</body>
</html>

