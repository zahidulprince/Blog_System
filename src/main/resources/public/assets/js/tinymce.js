tinymce.init({
    selector: "#mytextarea",

    branding: false,

    skin: 'oxide-dark',
    content_css: 'dark',

    plugins: [
		"advlist autolink link image imagetools lists charmap print preview hr anchor pagebreak spellchecker codesample",
		"searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreaking",
        "save table contextmenu directionality emoticons template paste textcolor code help casechange",
        "lists checklist autosave"
    ],

    mobile: {
        menubar: true
    },

    

    /* width and height of the editor */
	width: "100%",
	height: 400,
	
	/* display statusbar */
    statubar: true,

    autosave_interval: "20s",
    autosave_restore_when_empty: true,
    autosave_retention: "3000m",

    /* toolbar */

    toolbar: 'undo redo restoredraft codesample | styleselect casechange | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image | print preview media fullpage | forecolor backcolor emoticons',

});
