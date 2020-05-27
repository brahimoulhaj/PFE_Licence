<?php include('header.php'); ?>

<?php 
require('../sihaty/ConnectDB.php');
	$id = $_POST["dos_id"];
	$data = $conn->query("SELECT * FROM dossiers WHERE id=$id");

	while($result = $data->fetch(PDO::FETCH_ASSOC)){
		$description = $result["description"];
		$specialite = $result["specialite"];
		$pat_id = $result["patient_id"];
	}
?>

<main>
	<center>
		<div class="formulaire">
			<form method=post action="DossierEditedSubmit.php">
				N° Dossier:<br>
				<input type="text" name="dos_id" value="<?php	echo $id; ?>" readonly><br><br>
				<select name="specialite" required>
				<?php 
				require('../sihaty/ConnectDB.php');
				$data = $conn->query("SELECT * FROM specialites");
				while($donnee = $data->fetch(PDO::FETCH_ASSOC)){
					echo '<option value="'.$donnee["specialite"].'">'.$donnee["specialite"].'</option>';
				}
				?>
				</select><br><br>
				Description:<br>
				<textarea rows="8" cols="70" name="description" required><?php	echo $description; ?></textarea>
				<br><br>
				<button type="submit">Valider</button>
			</form><br>
			<a href="Dossier.php"><button>Annuler</button></a>
		</div>
	</center>
</main>

<?php include('end.php'); ?>