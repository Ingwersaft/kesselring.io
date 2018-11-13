package io.kesselring

val header = """<!DOCTYPE html>
<html lang="en" class="">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Marcel Kesselring - Softwareingenieur</title>
    <script src="kesselringio.bundle.js"></script>
    <script defer src="https://use.fontawesome.com/releases/v5.3.1/js/all.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bulma/0.7.2/css/bulma.min.css"/>
    <link rel="stylesheet" href="kesselring.css"/>
    <link href='https://fonts.googleapis.com/css?family=Montserrat' rel='stylesheet'>
</head>
<body>
<!-- start-->
<section id="home">
    <div id="headertext">
        <h1>Marcel Kesselring</h1>
        <p>
            <a href="https://github.com/Ingwersaft" target="_blank">
                <i class="fab fa-github" style="color: white"></i>
            </a>
            <a href="https://www.xing.com/profile/Marcel_Kesselring2" target="_blank">
                <i class="fab fa-xing" style="color: white"></i>
            </a>
            <a href="https://www.linkedin.com/in/marcel-kesselring-6a05b1173/" target="_blank">
                <i class="fab fa-linkedin" style="color: white"></i>
            </a>
            <a href="mailto:marcel@kesselring.io" target="_blank">
                <i class="far fa-envelope" style="color: white"></i>
            </a>
        </p>
    </div>
    <div id="left"></div>
    <div id="right"></div>
</section>
<!-- floating home button -->
<div id="upButton">
    <a href="index.html#nav" class="button">
    <span class="icon is-small is-rounded">
      <i class="fas fa-home"></i>
    </span>
    </a>
</div>
"""

val footer = """<footer class="footer">
    <div class="content has-text-centered">
        <div>
            <a class="link" href="impressum.html#left">Impressum</a>
        </div>
        <div>
            <a class="link" href="datenschutz.html#left">Datenschutzerklärung</a>
        </div>
        <div>
            <p>© 2018 Marcel Kesselring</p>
        </div>
        <br>
        <div>
            <p>
                Designed by <a class="link" href="https://kesselring.io">Marcel Kesselring</a>. The website source code
                is
                publicly
                developed at
                <a class="link" href="todo">Ingwersaft/kesselring.io</a>
            </p>
        </div>
    </div>
</footer>
<script src="https://unpkg.com/vh-check/dist/vh-check.min.js"></script>
<script>
    (function () {
        // initialize the test; leave in!
        var test = vhCheck('vh-offset');
    }());
</script>
<!-- end-->
</body>
"""