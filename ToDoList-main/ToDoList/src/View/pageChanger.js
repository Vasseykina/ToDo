
var cur_page ;
var pages_num ;
var contributorName;
var shareIds = [];


$("#NewButton").click(function() {
    $("#ListTitle").show();
  });
  
  $("#ListAddButton").click(function() {
    AddNewList();
  });

  $("#ShowShareInputButton").click(function() {
    $("#ShareInput").show();
    $("#ShowShareInputButton").hide();
  });

function showShareInput(){

}

function showCheckBoxes(size){
    contributorName = document.getElementById("ContributorInput").value;
    if(contributorName != null && contributorName != ""){

        for(i = 0; i < size; i++)
            $("#" + i + "Checkbox").show();

        $("#ShareInput").hide();
        $("#DoneButton").show();
    }
    
}


function stopSharing(id){
    var xhr = new XMLHttpRequest();
    var data =  "state=stopSharing"  + "&" + "id=" + id;
    xhr.onreadystatechange = function(){
        if(this.readyState == 4 && this.status == 200){
            location.reload();
        }

    }

    xhr.open("POST", "/ToDoList/ToDoList", true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.send(data);

}


function shareDone(){
    var xhr = new XMLHttpRequest();
    if(contributorName != null && contributorName != "" && shareIds != null && shareIds.length != 0){

        var ids = "";
        for( i = 0; i < shareIds.length; i++){
            if(i==shareIds.length - 1)
                ids = ids + shareIds[i].toString();
            else
                ids = ids + shareIds[i].toString() + ":";

        }
        var data =  "state=ShareList"  + "&" + "user="+ contributorName + "&" + "ids=" + ids;

        xhr.onreadystatechange = function(){
            if(this.readyState == 4 && this.status == 200){
                location.reload();
                contributorName = "";
                shareIds = [];
            }

        }

        xhr.open("POST", "/ToDoList/ToDoList", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.send(data);

    }
    else
        location.reload();

    


}



function shareListsChoose(id){


    if(document.getElementById(id + "CheckboxVal").checked)
        shareIds.push(id);
    
    else
        shareIds.splice(indexOf(id));


}



function AddNewList(){
    var xhr = new XMLHttpRequest();
    var Title = document.getElementById("TitleInput").value;
    if(Title != null && Title != ""){
        var data =  "state=AddList" + "&" + "title=" + Title;

        xhr.onreadystatechange = function(){
            if(this.readyState == 4 && this.status == 200){
                location.reload();
            }

        }

        xhr.open("POST", "/ToDoList/ToDoList", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.send(data);

    }

}

function DeleteList(id){

    var xhr = new XMLHttpRequest();
     
    var data =  "state=DeleteList" + "&" + "id=" + id;

        xhr.onreadystatechange = function(){
            if(this.readyState == 4 && this.status == 200){
                location.reload();
            }

        }

        xhr.open("POST", "/ToDoList/ToDoList", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.send(data);

}

function showList(id){
    var sub = document.getElementById(id + "SubLists");
    var button = document.getElementById(id + "OpenButton");
    if($(sub).is(":hidden")){
        $(sub).show();
        //button.value = "Close";
   }
    else{
        $(sub).hide();
        //button.value = "Open";
    }
}

function showAddSubList(id){
    $("#" + id +"SubText").show();
}

function addSubList(id){

    var xhr = new XMLHttpRequest();
    var title = document.getElementById(id + "SubTextInput").value;

    if(title != "" && title != null){

         var data =  "state=addSubList" + "&" + "id=" + id + "&" + "title=" + title;

        xhr.onreadystatechange = function(){
            if(this.readyState == 4 && this.status == 200){
                location.reload();
            }

        }

        xhr.open("POST", "/ToDoList/ToDoList", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.send(data);
    }

}

function addSharedSubList(name, id){

    var xhr = new XMLHttpRequest();
    var title = document.getElementById(id + "SubTextInput").value;

    if(title != "" && title != null){

         var data =  "state=addSharedSubList"  + "&" + "name=" + name + "&" + "id=" + id + "&" + "title=" + title;

        xhr.onreadystatechange = function(){
            if(this.readyState == 4 && this.status == 200){
                location.reload();
            }

        }

        xhr.open("POST", "/ToDoList/Shared", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.send(data);
    }

}

function DeleteSubList(i, j){
    var xhr = new XMLHttpRequest();
    var data =  "state=deleteSubList" + "&" + "titleId=" + i + "&" + "subId=" + j;

    xhr.onreadystatechange = function(){
        if(this.readyState == 4 && this.status == 200){
            location.reload();
        }
    }

    xhr.open("POST", "/ToDoList/ToDoList", true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.send(data);

}

function DeleteSharedSubList(name, i, j){
    var xhr = new XMLHttpRequest();
    var data =  "state=deleteSharedSubList"  + "&" + "name=" + name + "&" + "titleId=" + i + "&" + "subId=" + j;

    xhr.onreadystatechange = function(){
        if(this.readyState == 4 && this.status == 200){
            location.reload();
        }
    }

    xhr.open("POST", "/ToDoList/Shared", true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.send(data);

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


        if(cur_page == 1 ){
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

