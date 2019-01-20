<html>
<head>
<meta charset="utf-8">
<meta content="text/html; charset=UTF-8" http-equiv="content-type"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="cache-control" content="max-age=0" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
<meta http-equiv="pragma" content="no-cache" />
<title>${username}'s NMO WEB UI :: ${system}</title>
<!--<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">-->
<link href="https://maxcdn.bootstrapcdn.com/bootswatch/3.3.7/darkly/bootstrap.min.css" rel="stylesheet" integrity="sha384-S7YMK1xjUjSpEnF4P8hPUcgjXYLZKK3fQW1j5ObLSl787II9p8RO9XUGehRmKsxd" crossorigin="anonymous">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jasny-bootstrap/3.1.3/css/jasny-bootstrap.css" integrity="sha256-t0GDqKZnpVh1OVTSGzI7i52hYH9DN68z81MATIqktRo=" crossorigin="anonymous" />
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css" integrity="sha256-3iu9jgsy9TpTwXKb7bNQzqWekRX7pPK+2OLj3R922fo=" crossorigin="anonymous" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.2/css/bootstrap-select.min.css" integrity="sha256-an4uqLnVJ2flr7w0U74xiF4PJjO2N5Df91R2CUmCLCA=" crossorigin="anonymous" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ekko-lightbox/5.1.1/ekko-lightbox.min.css" integrity="sha256-8aNQFtmxcOMVoOhLD4mrHqaKC2Ui++LmlQsoKTqxwIE=" crossorigin="anonymous" />
<!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->
<style>
form {
	display: inline-block;
	margin: 1px;
}
.nmo-rhs-pane > form {
	margin: 1px -1px;
}
.nmo-action-button {
	width: 348px !important;
}
.nmo-pause-button {
	width: 143px !important;
}
.nmo-unpause-button {
	width: 433px !important;
}
.btn {
	padding: 3px !important;
}
.btn-nmo-orange { 
  color: #ffffff; 
  background-color: #D97F4F; 
  border-color: #D4703A; 
} 
.btn-nmo-orange:hover, 
.btn-nmo-orange:focus, 
.btn-nmo-orange:active, 
.btn-nmo-orange.active, 
.open .dropdown-toggle.btn-nmo-orange { 
  color: #ffffff; 
  background-color: #C9622C; 
  border-color: #D4703A; 
} 
.btn-nmo-orange:active, 
.btn-nmo-orange.active, 
.open .dropdown-toggle.btn-nmo-orange { 
  background-image: none; 
} 
.btn-nmo-orange.disabled, 
.btn-nmo-orange[disabled], 
fieldset[disabled] .btn-nmo-orange, 
.btn-nmo-orange.disabled:hover, 
.btn-nmo-orange[disabled]:hover, 
fieldset[disabled] .btn-nmo-orange:hover, 
.btn-nmo-orange.disabled:focus, 
.btn-nmo-orange[disabled]:focus, 
fieldset[disabled] .btn-nmo-orange:focus, 
.btn-nmo-orange.disabled:active, 
.btn-nmo-orange[disabled]:active, 
fieldset[disabled] .btn-nmo-orange:active, 
.btn-nmo-orange.disabled.active, 
.btn-nmo-orange[disabled].active, 
fieldset[disabled] .btn-nmo-orange.active { 
  background-color: #D97F4F; 
  border-color: #D4703A; 
} 
.btn-nmo-orange .badge { 
  color: #D97F4F; 
  background-color: #ffffff; 
}
.btn-nmo-gold { 
  color: #ffffff; 
  background-color: #D9AB4F; 
  border-color: #D4A13A; 
} 
.btn-nmo-gold:hover, 
.btn-nmo-gold:focus, 
.btn-nmo-gold:active, 
.btn-nmo-gold.active, 
.open .dropdown-toggle.btn-nmo-gold { 
  color: #ffffff; 
  background-color: #C9942C; 
  border-color: #D4A13A; 
} 
.btn-nmo-gold:active, 
.btn-nmo-gold.active, 
.open .dropdown-toggle.btn-nmo-gold { 
  background-image: none; 
} 
.btn-nmo-gold.disabled, 
.btn-nmo-gold[disabled], 
fieldset[disabled] .btn-nmo-gold, 
.btn-nmo-gold.disabled:hover, 
.btn-nmo-gold[disabled]:hover, 
fieldset[disabled] .btn-nmo-gold:hover, 
.btn-nmo-gold.disabled:focus, 
.btn-nmo-gold[disabled]:focus, 
fieldset[disabled] .btn-nmo-gold:focus, 
.btn-nmo-gold.disabled:active, 
.btn-nmo-gold[disabled]:active, 
fieldset[disabled] .btn-nmo-gold:active, 
.btn-nmo-gold.disabled.active, 
.btn-nmo-gold[disabled].active, 
fieldset[disabled] .btn-nmo-gold.active { 
  background-color: #D9AB4F; 
  border-color: #D4A13A; 
} 
.btn-nmo-gold .badge { 
  color: #D9AB4F; 
  background-color: #ffffff; 
}
.btn-nmo-purp { 
  color: #ffffff; 
  background-color: #685395; 
  border-color: #5F4790; 
}
.btn-nmo-purp:hover, 
.btn-nmo-purp:focus, 
.btn-nmo-purp:active, 
.btn-nmo-purp.active, 
.open .dropdown-toggle.btn-nmo-purp { 
  color: #ffffff; 
  background-color: #553D87; 
  border-color: #5F4790; 
} 
.btn-nmo-purp:active, 
.btn-nmo-purp.active, 
.open .dropdown-toggle.btn-nmo-purp { 
  background-image: none; 
} 
.btn-nmo-purp.disabled, 
.btn-nmo-purp[disabled], 
fieldset[disabled] .btn-nmo-purp, 
.btn-nmo-purp.disabled:hover, 
.btn-nmo-purp[disabled]:hover, 
fieldset[disabled] .btn-nmo-purp:hover, 
.btn-nmo-purp.disabled:focus, 
.btn-nmo-purp[disabled]:focus, 
fieldset[disabled] .btn-nmo-purp:focus, 
.btn-nmo-purp.disabled:active, 
.btn-nmo-purp[disabled]:active, 
fieldset[disabled] .btn-nmo-purp:active, 
.btn-nmo-purp.disabled.active, 
.btn-nmo-purp[disabled].active, 
fieldset[disabled] .btn-nmo-purp.active { 
  background-color: #685395; 
  border-color: #5F4790; 
} 
.btn-nmo-purp .badge { 
  color: #685395; 
  background-color: #ffffff; 
}
.nmo-lhs-pane {
	max-width: 700px;
	margin-right: 440px;
}
.nmo-rhs-pane {
 	width: 430px;
}
@media (min-width: 768px) {
 .nmo-rhs-pane {
 	display: block;
 	clear: both;
 	float: none;
 }
}
@media (min-width: 992px) {
 .nmo-rhs-pane {
 	float: right;
 	margin-top: -10px;
 	text-align: right;
 }
}
.btn-basic {
    background-color: #444;
    color: white;
}
.btn-basic:hover {
    background-color: #333;
    color: white;
}
.btn-basic:focus {
    background-color: #333;
    color: white;
}
</style>
</head>
<body>
<!-- Modal -->
<div class="modal fade" id="nmo-pause-modal" tabindex="-1" role="dialog" aria-labelledby="nmo-pause-modalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title">Pause for <span id="nmo-pause-length-1">DURATION</span></h4>
      </div>
      <form data-toggle="validator" id="nmo-pause-form" method="POST" data-js-ajax-form="true" action="" class="form-horizontal" role="form" style="width: 100%">
      	<div class="modal-body" style="padding: 10px;">
		    <div style="padding: 10px; background-color: #aa0000; text-align: center;">
		    	<h1 style="margin: 0; font-weight: bolder; font-size: 48px;"><i class="fa fa-exclamation-triangle" aria-hidden="true" style="padding-right:22px"></i>STOP AND THINK !!</h1>
				<h3 style="margin: 10 0 0 0; font-weight: bold; line-height: 36px;">DO YOU REALLY NEED TO PAUSE<br>FOR <span id="nmo-pause-length-2">DURATION</span> ?</h3>
			</div>
			<div style="padding: 10px 10px 0px 10px;font-size: 21px;">
		        <p style="margin: 10px 0px 20px 0px">If you are absolutely sure you need to pause:</p>
					<div class="form-group">
						<label class="col-sm-5 control-label" for="reason">Input pause reason:</label>
						<div class="col-sm-7">
							<input type="text" class="form-control" id="reason" name="reason" placeholder="" required>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-5 control-label" for="captcha">Solve captcha:</label>
						<div class="col-sm-4">
							<img id="captcha-image" src="" width=160 height=50 />
						</div>
						<div class="col-sm-3">
							<input data-remote="/path/to/remote/validator?test=stuff" type="text" class="form-control" id="captcha" name="captcha" placeholder="" required>
						</div>
					</div>
		    </div>
		</div>
      <div class="modal-footer">
        <button type="submit" class="btn btn-lg btn-primary" style="padding: 10px !important;">Confirm pause</button>
      </div>
    </form>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<div class="container">
<h1>${username}'s NMO <small>- NoMoreOversleeps Web UI v${version}</small> <a href="https://github.com/PolyphasicDevTeam/NoMoreOversleeps"><button type="submit" class="btn-sm btn-primary" style="padding:2px; position:relative; top:-4px;">Download source</button></a></h1>
  <div style="clear:both; padding-top:4px;"></div>
  <h4>Status</h4>
  <table class="table table-striped">
    <tbody>
      <tr>
        <td><strong>Last refresh</strong></td>
        <td id="nmo_last_refresh">never</td>
      </tr>
      <tr>
        <td style="width:230px;"><strong>Schedule</strong></td>
        <td id="nmo_schedule_name">?</td>
      </tr>
      <tr>
        <td style="width:230px;"><strong>Current part of schedule</strong></td>
        <td id="nmo_schedule">?</td>
      </tr>
      <tr>
        <td><strong>Activity detection state</strong></td>
        <td id="nmo_pause_state">?</td>
      </tr>
      <tr>
        <td style="width:230px;"><strong>Active timer</strong></td>
        <td id="nmo_active_timer">?</td>
      </tr>
      <tr>
        <td style="width:230px;"><strong>Last activity detected</strong></td>
        <td id="nmo_activity">?</td>
      </tr>
      <tr>
        <td style="width:230px;"><strong>Zombie trapper</strong></td>
        <td id="nmo_zombie_state">?</td>
      </tr>
      <tr>
        <td style="width:230px;"><strong>Camera sockets open</strong></td>
        <td id="nmo_conn_count">?</td>
      </tr>
      <#if integration_noise>
      <tr>
        <td><strong>Annoying noise</strong></td>
        <td id="nmo_noise_state">?</td>
      </tr>
      </#if>
      <#if integration_philipsHue || integration_tplink || integration_wemo>
      <tr>
        <td><strong>Devices</strong></td>
        <td id="nmo_ha_state">?</td>
      </tr>
      </#if>
    </tbody>
  </table>
  <hr/>
  <div>
  ${message}
  </div>
  <hr/>
  <div class="nmo-rhs-pane">
  	<#list webcams as cam>
  	<h4>${cam}</h4>
  	<img id="webcamImage_${cam?index}" height=320/>
  	</#list>
  </div>
  <#if integration_pause>
  <div class="nmo-rhs-pane" style="margin-top: 10px">
  	<h4>Pause controls</h4>
	<form><button type="button" class="btn btn-basic nmo-pause-button" onclick="clickPauseButton(5, '5m');">5m</button></form>
  	<form><button type="button" class="btn btn-basic nmo-pause-button" onclick="clickPauseButton(10, '10m');">10m</button></form>
  	<form><button type="button" class="btn btn-basic nmo-pause-button" onclick="clickPauseButton(15, '15m');">15m</button></form>
  	<form><button type="button" class="btn btn-basic nmo-pause-button" onclick="clickPauseButton(20, '20m');">20m</button></form>
  	<form><button type="button" class="btn btn-basic nmo-pause-button" onclick="clickPauseButton(25, '25m');">25m</button></form>
  	<form><button type="button" class="btn btn-basic nmo-pause-button" onclick="clickPauseButton(30, '30m');">30m</button></form>
  	<form><button type="button" class="btn btn-basic nmo-pause-button" onclick="clickPauseButton(45, '45m');">45m</button></form>
  	<form><button type="button" class="btn btn-basic nmo-pause-button" onclick="clickPauseButton(60, '1h');">1h</button></form>
  	<form><button type="button" class="btn btn-basic nmo-pause-button" onclick="clickPauseButton(90, '1h30m');">1h30m</button></form>
  	<form><button type="button" class="btn btn-basic nmo-pause-button" onclick="clickPauseButton(105, '1h45m');">1h45m</button></form>
  	<form><button type="button" class="btn btn-basic nmo-pause-button" onclick="clickPauseButton(120, '2h');">2h</button></form>
  	<form><button type="button" class="btn btn-basic nmo-pause-button" onclick="clickPauseButton(180, '3h');">3h</button></form>
  	<form><button type="button" class="btn btn-basic nmo-pause-button" onclick="clickPauseButton(240, '4h');">4h</button></form>
  	<form><button type="button" class="btn btn-basic nmo-pause-button" onclick="clickPauseButton(300, '5h');">5h</button></form>
  	<form><button type="button" class="btn btn-basic nmo-pause-button" onclick="clickPauseButton(360, '6h');">6h</button></form>
  	<form><button type="button" class="btn btn-basic nmo-pause-button" onclick="clickPauseButton(420, '7h');">7h</button></form>
  	<form><button type="button" class="btn btn-basic nmo-pause-button" onclick="clickPauseButton(480, '8h');">8h</button></form>
  	<form><button type="button" class="btn btn-basic nmo-pause-button" onclick="clickPauseButton(540, '9h');">9h</button></form>
  	<form><button type="button" class="btn btn-basic nmo-pause-button" onclick="clickPauseButton(600, '10h');">10h</button></form>
  	<form><button type="button" class="btn btn-basic nmo-pause-button" onclick="clickPauseButton(660, '11h');">11h</button></form>
  	<form><button type="button" class="btn btn-basic nmo-pause-button" onclick="clickPauseButton(720, '12h');">12h</button></form>
  	<form method="POST" data-js-ajax-form="true" action="/ui/pause/0"><button type="submit" class="btn btn-basic nmo-unpause-button">UNPAUSE</button></form>
  </div>
  </#if>
  <div class="nmo-lhs-pane">
  <h4>Manual controls</h4>
  	${actionButtons}
  </div>
  <div></div>
  <div style="clear:both;"></div>
  <h4>Log</h4>
  <pre id="nmo_log_container"></pre>
</div>
<script src="https://code.jquery.com/jquery-2.1.4.min.js" integrity="sha384-R4/ztc4ZlRqWjqIuvf6RX5yb/v90qNGx6fS48N0tRxiGkqveZETq72KgDVJCp2TC" crossorigin="anonymous" type="text/javascript"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous" type="text/javascript"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jasny-bootstrap/3.1.3/js/jasny-bootstrap.js" integrity="sha256-NG7ORCRPjgqoYm8LQoOuJjXtFW2++/ElxKFup4JwXS8=" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/1000hz-bootstrap-validator/0.11.9/validator.min.js" integrity="sha256-dHf/YjH1A4tewEsKUSmNnV05DDbfGN3g7NMq86xgGh8=" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.2/js/bootstrap-select.min.js" integrity="sha256-JD3g+rB9BjW6/cGEuwCue1sGtitb2aQVNs/pl4114XQ=" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/ekko-lightbox/5.1.1/ekko-lightbox.min.js" integrity="sha256-1odJPEl+KoMUaA1T7QNMGSSU/r5LCKCRC6SL8P0r2gY=" crossorigin="anonymous"></script>
<script>
!function(a,b){"function"==typeof define&&define.amd?define([],b):"undefined"!=typeof module&&module.exports?module.exports=b():a.ReconnectingWebSocket=b()}(this,function(){function a(b,c,d){function l(a,b){var c=document.createEvent("CustomEvent");return c.initCustomEvent(a,!1,!1,b),c}var e={debug:!1,automaticOpen:!0,reconnectInterval:1e3,maxReconnectInterval:3e4,reconnectDecay:1.5,timeoutInterval:2e3};d||(d={});for(var f in e)this[f]="undefined"!=typeof d[f]?d[f]:e[f];this.url=b,this.reconnectAttempts=0,this.readyState=WebSocket.CONNECTING,this.protocol=null;var h,g=this,i=!1,j=!1,k=document.createElement("div");k.addEventListener("open",function(a){g.onopen(a)}),k.addEventListener("close",function(a){g.onclose(a)}),k.addEventListener("connecting",function(a){g.onconnecting(a)}),k.addEventListener("message",function(a){g.onmessage(a)}),k.addEventListener("error",function(a){g.onerror(a)}),this.addEventListener=k.addEventListener.bind(k),this.removeEventListener=k.removeEventListener.bind(k),this.dispatchEvent=k.dispatchEvent.bind(k),this.open=function(b){h=new WebSocket(g.url,c||[]),b||k.dispatchEvent(l("connecting")),(g.debug||a.debugAll)&&console.debug("ReconnectingWebSocket","attempt-connect",g.url);var d=h,e=setTimeout(function(){(g.debug||a.debugAll)&&console.debug("ReconnectingWebSocket","connection-timeout",g.url),j=!0,d.close(),j=!1},g.timeoutInterval);h.onopen=function(){clearTimeout(e),(g.debug||a.debugAll)&&console.debug("ReconnectingWebSocket","onopen",g.url),g.protocol=h.protocol,g.readyState=WebSocket.OPEN,g.reconnectAttempts=0;var d=l("open");d.isReconnect=b,b=!1,k.dispatchEvent(d)},h.onclose=function(c){if(clearTimeout(e),h=null,i)g.readyState=WebSocket.CLOSED,k.dispatchEvent(l("close"));else{g.readyState=WebSocket.CONNECTING;var d=l("connecting");d.code=c.code,d.reason=c.reason,d.wasClean=c.wasClean,k.dispatchEvent(d),b||j||((g.debug||a.debugAll)&&console.debug("ReconnectingWebSocket","onclose",g.url),k.dispatchEvent(l("close")));var e=g.reconnectInterval*Math.pow(g.reconnectDecay,g.reconnectAttempts);setTimeout(function(){g.reconnectAttempts++,g.open(!0)},e>g.maxReconnectInterval?g.maxReconnectInterval:e)}},h.onmessage=function(b){(g.debug||a.debugAll)&&console.debug("ReconnectingWebSocket","onmessage",g.url,b.data);var c=l("message");c.data=b.data,k.dispatchEvent(c)},h.onerror=function(b){(g.debug||a.debugAll)&&console.debug("ReconnectingWebSocket","onerror",g.url,b),k.dispatchEvent(l("error"))}},1==this.automaticOpen&&this.open(!1),this.send=function(b){if(h)return(g.debug||a.debugAll)&&console.debug("ReconnectingWebSocket","send",g.url,b),h.send(b);throw"INVALID_STATE_ERR : Pausing to reconnect websocket"},this.close=function(a,b){"undefined"==typeof a&&(a=1e3),i=!0,h&&h.close(a,b)},this.refresh=function(){h&&h.close()}}return a.prototype.onopen=function(){},a.prototype.onclose=function(){},a.prototype.onconnecting=function(){},a.prototype.onmessage=function(){},a.prototype.onerror=function(){},a.debugAll=!1,a.CONNECTING=WebSocket.CONNECTING,a.OPEN=WebSocket.OPEN,a.CLOSING=WebSocket.CLOSING,a.CLOSED=WebSocket.CLOSED,a});
</script>
<script>
	/**
	*	Returns the function indicated by 'function_name_string'
	*	if it exists.
	*	
	*	Returns undefined if it does not (or a non-string argument is passed).
	*/
	window.getFunctionFromString = function(function_name_string)
	{
		if (typeof function_name_string !== 'string') return undefined;
		
	    var scope = window;
	    var function_name_split = function_name_string.split('.');
	    while (function_name_split.length > 0) {
	    	scope = scope[function_name_split.shift()];
	    	if (scope == undefined) return undefined;
	    }
	    return scope;
	}

	function clickPauseButton(durationValue, durationText)
	{
		var seed = "" + Math.floor(Math.random() * 123456789)
		$('#nmo-pause-length-1').text(durationText);
		$('#nmo-pause-length-2').text(durationText);
		$('#reason').val('');
		$('#captcha').val('');
		$("#captcha").attr("data-remote", "/validate-captcha?seed=" + seed);
		$('#captcha-image').attr('src', "/captcha.png?seed=" + seed);
		$("#nmo-pause-form").attr("action", "/ui/pause/" + durationValue);
		$('.form-group').removeClass('has-error has-feedback has-danger');
		$('#nmo-pause-modal').modal('show');
	}

    $(document).ready(function() {
        function pollState() {
            $.ajax({
                url: "/ui/json",
                dataType: "text",
                cache: false,
                success: function(data) {
                    var json = $.parseJSON(data);
                    $('#nmo_last_refresh').html(json.update);
                    $('#nmo_activity').html(json.activity);
                    $('#nmo_active_timer').html(json.active_timer);
                    $('#nmo_pause_state').html(json.pause_state);
                    $('#nmo_conn_count').html(json.conn_count);
                    $('#nmo_schedule').html(json.schedule);
                    $('#nmo_schedule_name').html(json.schedule_name);
                    $('#nmo_noise_state').html(json.noise_state);
                    $('#nmo_ha_state').html(json.ha_state);
                    $('#nmo_zombie_state').html(json.zombie_state);
                }
            });
            
            $.get("/ui/log", function(log){
            	$("#nmo_log_container").text(log);
            });
        }
        pollState();
        setInterval(pollState, 1863);
        
        /**
        * 	Any form with the data-js-ajax-form data attribute
        * 	will be submitted using AJAX, using the correct submission method.
        * 	The extra parameter 'ajax_form=true' will be readable 
        * 	to differentiate from a normal request on the server-side.
        * 
        * 	On successful response, the function in the 'data-js-ajax-form-callback' will be called,
        * 	with the response object as argument.
        *	(If this data attribute is not provided, nothing will be called.)
        *
        */
        $("form[data-js-ajax-form]").validator().on( "submit", function(event){
        	if (!event.isDefaultPrevented()) {
	        	event.preventDefault();
	        	var form_data = $(this).serialize();
	        	if (form_data != '') {
	           	    form_data += '&';
	        	}
	        	form_data += 'ajax_form=true';
	        	var callback_fun = getFunctionFromString($(this).data('js-ajax-form-callback'));
	        	        	
	        	$.ajax($(this).attr('action'), {
	        	    method: $(this).attr('method'),
	        	    data: form_data
	        	}).done(function(ajax_result) {
	        		if (typeof callback_fun === 'function') {
	        			callback_fun(ajax_result);
	        		}
	        	});
	        	
	        	$('#nmo-pause-modal').modal('hide');
	        }
        });

		var socket_security = 'ws';
		if (location.protocol === 'https:')
		{
			socket_security = 'wss';
		}
		<#list 0..(camTotal-1) as camID>
			var webcam_websocket_${camID} = new ReconnectingWebSocket("${webcamURL}?key=${webcamKey}&camID=${camID}");
			webcam_websocket_${camID}.onopen = function(e) {
				if (typeof console !== 'undefined') {
					console.info('webcam_websocket_${camID} open');
				}
			};
			webcam_websocket_${camID}.onmessage = function (e) {
				var data = JSON.parse(e.data), type = data.type, i = 0, $webcams = $('#webcams'), $img = null;
				if (typeof console !== 'undefined') {
					console.info('webcam_websocket_${camID} message', type);
				}
				$img = $('#webcamImage_${camID}');
				$img.attr("src", "data:image/jpeg;base64," + data.image).addClass('shadow').trigger("change");
				setTimeout(function() {
					$img.removeClass('shadow').trigger("change");
				}, 1000);
			};
			webcam_websocket_${camID}.onclose = function() {
				if (typeof console !== 'undefined') {
					console.info('webcam_websocket_${camID} close');
				}
			};
			webcam_websocket_${camID}.onerror = function(err) {
				if (typeof console !== 'undefined') {
					console.info('webcam_websocket_${camID} error ' + err.data + ' ' + err.code + ' ' + err.message + ' ' + err + ' ' + err.detail + ' ' + err.originalEvent);
				}
			};
		</#list>
	});
</script>
</body>
</html>
