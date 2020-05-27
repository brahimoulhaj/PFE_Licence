
<?php
			echo "loading...";

require('../sihaty/ConnectDB.php');
			if(isset($_POST["id"]) && isset($_POST["specialite"]) && isset($_POST["jour"]) && isset($_POST["heure"])){
				$rdvId = $_POST["id"];
				$rdvSp = $_POST["specialite"];
				$rdvJr = $_POST["jour"];
				$rdvHr = $_POST["heure"];
				$conn->exec("UPDATE rendezvous SET specialite='$rdvSp', jour='$rdvJr', heure='$rdvHr' WHERE id=$rdvId");
			}
?>

<script>
	
	window.location.href = "rendezvous.php";
</script>