<script src="script.js"></script>
<div class="main-header">
    <center>
    <table class="main-table">
        <tr>
        <td>
        <a href=rendezvous.php><div class="params rendez-vous">
            <div class="notification notif_rdv" style="width:100%; height:15%;"></div>
            <hr size=1px style="margin:0; margin-bottom:7px" color="#eee">
            <center><div class="cercle cercleYellow"><i class="fa fa-calendar"></i></div></center>
            <p style="margin-top:7px; margin-bottom:0;">Rendez-vous</p>
            <hr size=1px color="#eee">
            <p class="description">Confirmer, Refuser, Ajouter<br> les rendez-vous</p>
				</div></a>
				</td>
        <td>
				<a href="Dossier.php"><div class="params dossiers">
					<div class="notification notif_doss" style="width:100%; height:15%;"></div>
            <hr size=1px style="margin:0; margin-bottom:7px" color="#eee">
            <center><div class="cercle cercleGreen"><i class="fa fa-folder-open"></i></div></center>
            <p style="margin-top:7px; margin-bottom:0;">Dossiers</p>
            <hr size=1px color="#eee">
            <p class="description">Créer, Supprimer, Modifier<br> les dossiers</p>		
				</div></a>
				</td>
        <td>
				<a href=client.php><div class="params patients">
					<div class="notification notif_pat" style="width:100%; height:15%;"></div>
            <hr size=1px style="margin:0; margin-bottom:7px" color="#eee">
            <center><div class="cercle cercleBlue"><i class="fa fa-users"></i></div></center>
            <p style="margin-top:7px; margin-bottom:0;">Clients</p>
            <hr size=1px color="#eee">
            <p class="description">Confirmer, Supprimer, Créer<br> un compte</p>
				</div></a>
				</td>
        </tr>
    </table>
    </center>
    </div>

		<center><div id="map"></div></center>
    <script>
        function initMap() {
        var myLatLng = {lat: 30.405849, lng: -9.544960};
        var map = new google.maps.Map(document.getElementById('map'), {
          zoom: 14,
          center: myLatLng
        });

        var marker = new google.maps.Marker({
          position: myLatLng,
          map: map,
          title: 'Cabinet Medical IBNZOHR'
        });
      }
		</script>
    <script async defer
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyD1D6L6P6Pz0m7OVmN9iOzks5sqe69_GHE&callback=initMap">
		</script>

<style>
	#map {
    border: 2px solid black;
    width:50%;
		height:300px;
		margin-right: 5%;
	}
</style>
