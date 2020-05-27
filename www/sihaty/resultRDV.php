<?php include("header.php"); ?>
<main class="main_search">
	<center><?php include('searchRDV.php'); ?></center><br>
<?php 

require('../sihaty/ConnectDB.php');

	if(!empty($_GET["search"])){
		$elem = $_GET["search"];
		$result = $conn->query("
					SELECT * FROM touslesrdv
					WHERE UPPER(cin)=UPPER('$elem')
					OR UPPER(specialite)=UPPER('$elem')
					OR extract(year from jour)= '$elem'
					OR UPPER(nom_prenom) LIKE UPPER('%$elem%')
		");
		while($cmp = $result->fetch(PDO::FETCH_ASSOC)){
			$response[] = $cmp;
		}
	}

?>
<center>
<table class="search_table">
	<tr>
		<th>Nom</th>
		<th>CIN</th>
		<th>Spécialité</th>
		<th>Jour</th>
		<th>Heure</th>
		<th>Confirmer</th>
	</tr>
<?php 
	foreach($response as $data){
?>
	<tr>
		<td><?php echo $data["nom_prenom"]; ?></td>
		<td><?php echo $data["cin"]; ?></td>
		<td><?php echo $data["specialite"]; ?></td>
		<td><?php echo $data["jour"]; ?></td>
		<td><?php echo $data["heure"]; ?></td>
		<td>
			<?php 
				if($data["isConfirmed"] === "0"){
						echo "<i style='color:red; font-size:24px' class='fa fa-times-circle'></i>";
				}else{echo "<i style='color:green; font-size:24px' class='fa fa-check-circle'></i>";}
			?>
		</td>
		<td style="padding-top:10px; padding-bottom:10px">
			<?php if($data["isConfirmed"] === "0"){ ?><form method=post action="rendezvous.php">
				<input hidden type="text" readonly name="rdv_id" value="<?php echo $data["id"]; ?>" >
				<input type="submit" name="action" value="Confirmer" onClick="confirme()">&nbsp;
				<input type="submit" name="action" value="Annuler" onClick="confirme()">
			</form>
			<?php }?>
		</td>
		<td style="padding-top:10px; padding-bottom:10px">
			<form method=post action="modifierRDV.php">
				<input hidden type="text" readonly name="rdv_id" value="<?php echo $data["id"]; ?>" >
				<input type="submit" value="Modifier">
			</form>
		</td>
	</tr>
<?php
	}
?>
</table>
</center>
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