<?php include('header.php'); ?>

<?php //load data from rendezvous table
require('../sihaty/ConnectDB.php');
	$id = $_POST["rdv_id"];
	$data = $conn->query("SELECT * FROM rendezvous WHERE id=$id");

	while($result = $data->fetch(PDO::FETCH_ASSOC)){
		$specialite = $result["specialite"];
		$jour = $result["jour"];
		$heure = $result["heure"];
	}
?>

<main>
	<center>
		<div class="formulaire">
			<form method=post action="submitRDV.php">
				N° du rendez-vous:<br>
				<input type="text" readonly name="id" value="<?php	echo $id; ?>"><br><br>
				Spécialité:<br> <input type="text" name="specialite" value="<?php	echo $specialite; ?>"><br><br>
				Le:<br> <input type="text" name="jour" value="<?php	echo $jour; ?>"><br><br>
				A:<br> <input type="text" name="heure" value="<?php	echo $heure; ?>"><br><br>
				<button type="submit">Valider</button>
			</form><br>
			<a href="rendezvous.php"><button>Annuler</button></a>
		</div>
	</center>
</main>

<?php include('end.php'); ?>
