<?php include("header.php"); ?>
<script src="script.js"></script>
<main class="main_search">
	<center><?php include('searchRDV.php'); ?></center>
	<br>
	<div class="rdv_table"></div>
</main>
<?php require('end.php'); ?>

<script type="text/javascript">
	function confirme(){
		<?php
require('../sihaty/ConnectDB.php');
			$id = $_POST["rdv_id"];
			$rdv = $conn->query("SELECT * FROM rendezvous WHERE id=$id");
			while($info = $rdv->fetch(PDO::FETCH_ASSOC)){
					$jour = $info["jour"];
					$heure = $info["heure"];
					$patient = $info["patient_ID"];
					$specialite = $info["specialite"];
			}
		
			if($_POST["action"] == "Annuler"){
					$conn->exec("
						INSERT INTO patient_notif(title, text, patient_ID)
						VALUES('Rendez-vous','Votre rendez-vous de $specialite le $jour est annulé.',$patient)
					");
					$conn->exec("DELETE FROM `rendezvous` WHERE id=$id");
			}else{
				$conn->exec(
					"INSERT INTO patient_notif(title, text, patient_ID)
					VALUES('Rendez-vous','Votre rendez-vous de $specialite le $jour à $heure est confirmé.',$patient)
					");

				$conn->exec("UPDATE rendezvous SET isConfirmed=1 WHERE id=$id");
			}	
		?>
	}
</script>