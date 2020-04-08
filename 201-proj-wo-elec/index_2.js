function addcon(){
/*  var cont = prompt("Please enter the contact user name", "e.x. Harry Potter");
  if (cont == null || cont == "") {//later need to cross check username with database
    txt = "That is not a valid input";
  } else {
    txt = cont + " was added to contacts";*/
    var btn = document.createElement("BUTTON");
    btn.style.backgroundColor = "#0078a1";
    /*btn.innerHTML = cont;*/
    btn.style.left = "34%";
    btn.innerHTML = "User";
    btn.style.height = "38px";
    btn.style.width = "128px";
    btn.style.border = "none";
    btn.style.color = "white";
    btn.style.textAlign = "center";
    btn.style.textDecoration = "none";
    btn.style.display = "table";
    btn.style.margin = "0 auto";
    btn.style.marginTop = "5px";
    btn.style.fontSize = "16px";
    //add an onclick for share

     document.getElementById("contacts").appendChild(btn);
  //}
//  alert(txt);
}

function addproj(){
  var btn = document.createElement("BUTTON");
  btn.style.backgroundColor = "#0078a1";
  btn.innerHTML = "New Project ";
  btn.style.left = "34%";
  btn.style.height = "38px";
  btn.style.width = "228px";
  btn.style.border = "none";
  btn.style.color = "white";
  btn.style.textAlign = "center";
  btn.style.textDecoration = "none";
  btn.style.display = "table";
  btn.style.margin = "0 auto";
  btn.style.marginTop = "5px";
  btn.style.fontSize = "16px";

   document.getElementById("projects").appendChild(btn);
   btn.setAttribute("Id", "bts");
   document.getElementById("bts").onclick = function() { location.href = "file_try.html"}
     btn.onClick = "file_try.html";
}
