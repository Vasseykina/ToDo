
var cur_page ;
var pages_num ;


$("#NewButton").click(function() {
    $("#ListTitle").show();
  });
  


function AddNewList(){
    var xhr = new XMLHttpRequest();
    var Title = document.getElementById("Titleinput").value;
    if(Title != null && Title != ""){
        var data =  "state=AddList" + "&" + "title=" + Title;

        xhr.onreadystatechange = function(){
            if(this.readyState == 4 && this.status == 200){
                $("#ListTitle").hide();
            }

        }

        xhr.open("POST", "/ToDoList/ToDoList", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.send(data);

    }

}



function ShowFirst(num){
    
    pages_num = num;
    cur_page = 1;

    for( var i = 2; i < num+1; i++ ){
        var p = document.getElementById(i);
        p.style.display = 'none';
    }

}

function Next(){
    var page = document.getElementById(cur_page);
    page.style.display = 'none';
    cur_page += 1;

    page = document.getElementById(cur_page);
    page.style.display = 'block';

    ShowNavButtons();
}

function Prev(){
    var page = document.getElementById(cur_page);
    page.style.display = 'none';
    cur_page -= 1;

    page = document.getElementById(cur_page);
    page.style.display = 'block';

    ShowNavButtons();
    
}

function ShowNavButtons(){
    var Pbutton = document.getElementById("PButton");
    var Nbutton = document.getElementById("NButton");

    if(cur_page == 1){
        Pbutton.style.display = 'none';
        Nbutton.style.display = 'block';
    }
    
    if(cur_page > 1 && cur_page < pages_num){
        Pbutton.style.display = 'block';
        Nbutton.style.display = 'block';
    }

    if(pages_num == cur_page){
        Nbutton.style.display = 'none';
        Pbutton.style.display = 'block';
    }
    
}