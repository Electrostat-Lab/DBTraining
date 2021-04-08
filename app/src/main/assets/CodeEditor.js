    /**
     * find the js docs for this API on : https://ace.c9.io/#nav=howto
     */
    var editor = ace.edit("editor");
    //do css work in js ( just like xml & java workout) using HTML DOM ApI : https://www.w3schools.com/whatis/whatis_htmldom.asp
    document.getElementById("editor").style.height = CodeEditor.getContainerHeight()+"px";
    editor.insert(CodeEditor.getCode());
    editor.setTheme("ace/theme/"+CodeEditor.getTheme());
    // all themes are available here : https://github.com/ajaxorg/ace/tree/master/lib/ace/theme
    editor.session.setMode("ace/mode/"+CodeEditor.getMode());
    editor.setHighlightActiveLine(true);
    editor.setAutoScrollEditorIntoView(false);
    editor.setReadOnly(true);