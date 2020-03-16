$(document).ready(function() {
    

    $("#js-mobile-nav").on('click', function(e){
        e.preventDefault();

        $("#menu").animate({
            opacity: 'show',
            height: 'show',
            marginTop: 'show',
            marginBottom: 'show',
            paddingTop: 'show',
            paddingBottom: 'show'
        })
    })

    $("#menuClose").on('click', function(e){
        e.preventDefault();

        $("#menu").animate({
            opacity: 'hide',
            height: 'hide',
            marginTop: 'hide',
            marginBottom: 'hide',
            paddingTop: 'hide',
            paddingBottom: 'hide'
        })
    })

    $("a.isDisabled").on('click', function (e) {
        e.preventDefault();
    })
});