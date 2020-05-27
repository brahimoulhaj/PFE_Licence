<?php include("header.php"); ?>
<main class="main_search">
<?php
require('../sihaty/ConnectDB.php');
if(!empty($_GET["search"])){
	$elem = $_GET["search"];
	$result = $conn->query("
					SELECT * FROM patients
					WHERE UPPER(cin)=UPPER('$elem')
					OR UPPER(telephone)=UPPER('$elem')
					OR UPPER(nom_prenom) LIKE UPPER('%$elem%')
					ORDER BY isConfirmed, id
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
		<th>Téléphone</th>
		<th>Date de naissance</th>
		<th>Email</th>
		<th>Date d'inscription</th>
		<th>Confirmé</th>
	</tr>
<?php 
	foreach($response as $data){
?>
	<tr>
		<td><?php echo $data["nom_prenom"]; ?></td>
		<td><?php echo $data["cin"]; ?></td>
		<td><?php echo $data["telephone"]; ?></td>
		<td><?php echo $data["date_naissance"]; ?></td>
		<td><?php echo $data["email"]; ?></td>
		<td><?php echo $data["date_inscription"]; ?></td>
		<td>
			<?php
				if($data["isConfirmed"] === "0"){
					echo "<i style='color:red; font-size:24px' class='fa fa-times-circle'></i>";
				}else{echo "<i style='color:green; font-size:24px' class='fa fa-check-circle'></i>";}
			?>
		</td>
		<td>
			<?php if($data["isConfirmed"] === "0"){ ?>
			<form method=post action="search.php?search=<?php echo $elem; ?>">
				<input hidden type="text" readonly name="user_id" value="<?php echo $data["id"]; ?>" >
				<input type="submit" name="action" value="Accepter" onClick="confirme()">
			</form>
			<?php }?>
		</td>
		<td>
			<form method=post action="modifierClient.php">
				<input hidden type="text" readonly name="user_id" value="<?php echo $data["id"]; ?>" >
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
			$id = $_POST["user_id"];
			$conn->exec("UPDATE patients SET isConfirmed=1 WHERE id=$id");
		?>
		window.location.reload(true);
	}
</script>