<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>ToDoList</title>

    <link href="src/View/ToDoListBackground.css" rel="stylesheet" type="text/css">

</head>
  <body>

    <%
      int page_count = 1;
      HttpSession s = (HttpSession)request.getAttribute("session");
      String name = (String)session.getAttribute("name");
    %>
        <a href="LogOut">Logout</a> |
        <hr>

        <input type="button" class="button" id="NewButton" value="Create new To Do List"/>
        <br>

          <div id="ListTitle" hidden>
          <input type="text" id="TitleInput" />
          <input id="SubmitButton" class="ListAddButton" type="button" value="Add" ><br>

          </div>
        <br>

        

    <%
     LinkedHashMap<String, ArrayList<String>> lists = (LinkedHashMap<String, ArrayList<String>>)request.getAttribute("lists");

     Set<String> keySet = lists.keySet();
     List<String> listKeys = new ArrayList<String>(keySet);


     int count = 0;
     int pages_num = 0;
     int size = 0;

     if((lists.keySet().size())%9 == 0 )
       pages_num = (lists.keySet().size())/9;
     else
       pages_num = (lists.keySet().size())/9 + 1;


      if(pages_num == 1)
         size = lists.keySet().size();

      else
         size = 16;


      if(lists.size()!=0){
      %>
      <body onload="ShowFirst(<%=pages_num%>); ShowNavButtons();">

      <%
        int i = 0;
        int j = 0;

        for(; i<pages_num; i++){
      %>
          <div id="<%=i+1%>">

            <%
            for(; j < size; j++){
            %>
              <ul class="product-wrapper">

                <li id="<%=listKeys.get(j)%>Title"> <%=listKeys.get(j)%> 
                <input type="button" class="button" id="<%=listKeys.get(j)%>OpenButton" value="Open"/> 
                <input type="button" class="button" id="<%=listKeys.get(j)%>DeleteButton" value="Delete"/>  
                <input type="button" class="button"  id="<%=listKeys.get(j)%>AddButton" value="Add"/>
                  <ul>
                    <%
                     ArrayList<String> sub = lists.get(listKeys.get(j));
                     if(!sub.isEmpty() && sub != null){
                      for(int k = 0; k < sub.size(); k++){
                    %>
                        <li id="<%=k%>Sub"> <%=sub.get(k)%> </li>

                      <%
                      }
                      %>
                    <%
                    }
                    %>
                  </ul>
                </li>
              </ul>
            <%
            }
            %>
         </div>
          <%
          if(i == pages_num-2)
            size+= lists.keySet().size() - (pages_num-1)*9;
          else
            size+=9;
        }

      }else{
    %>
      <p>To Do List is empty!</p>
    <%}
    %>

<%if(pages_num > 1){%>
<div class="Pbutton">
          <input type="button" class="Pbutton1" id="PButton" value="Previous" />    <input type="button" class="Pbutton2" id="NButton" value="Next" />

</div>
<%}%>


    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
    <script src="src/View/pageChanger.js"></script>
</body>
</html>
