<?php include('header.php'); ?>

<?php //load data from rendezvous table
require('../sihaty/ConnectDB.php');
	$id = $_POST["user_id"];
	$data = $conn->query("SELECT * FROM patients WHERE id=$id");

	while($result = $data->fetch(PDO::FETCH_ASSOC)){
		$username = $result["nom_prenom"];
		$usercin = $result["cin"];
		$usergsm = $result["telephone"];
		$userbirth = $result["date_naissance"];
		$useremail = $result["email"];
		$useraddress = $result["adresse"];
		//$photo = $result["photo"];
	}
?>

<main>
	<center>
		<div class="formulaire">
			<form method=post action="submitClient.php" enctype="multipart/form-data">
				N° de patient:<br>
				<input type="text" name="id" value="<?php	echo $id; ?>" readonly><br><br>
				Nom et prénom:<br>
				<input type="text" name="name" value="<?php	echo $username; ?>"><br><br>
				CIN:<br>
				<input type="text" name="cin" value="<?php	echo $usercin; ?>"><br><br>
				N° de téléphone:<br>
				<input type="text" name="gsm" value="<?php	echo $usergsm; ?>"><br><br>
				Date de naissance<br>
				<input type="text" name="birth" value="<?php	echo $userbirth; ?>"><br><br>
				Adresse<br>
				<input type="text" name="address" value="<?php	echo $useraddress; ?>"><br><br>
				Email<br>
				<input type="email" name="email" value="<?php	echo $useremail; ?>"><br><br>
				<!--?php if($photo == null){ ?>
				Photo<br>
				<input type="file" name="photo"><br><br-->
				<!--?php }?-->
				<button type="submit">Valider</button>
			</form><br>
			<a href="client.php"><button>Annuler</button></a>
		</div>
	</center>
</main>

<?php include('end.php'); ?>