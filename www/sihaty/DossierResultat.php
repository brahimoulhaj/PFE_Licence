<?php include("header.php"); ?>

<main class="main_search">
	<center><?php include('DossierSearch.php'); ?></center><br>
<?php 

require('../sihaty/ConnectDB.php');

	if(!empty($_GET["search"])){
		$elem = $_GET["search"];
		$result = $conn->query("
					SELECT * FROM touslesdos
					WHERE UPPER(cin)=UPPER('$elem')
					OR UPPER(specialite)=UPPER('$elem')
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
		<th>N° Dossier</th>
		<th>Date de création</th>
		<th>Description</th>
		<th>Spécialité</th>
		<th>Patient</th>
		<th>CIN</th>
	</tr>
<?php 
	foreach($response as $data){
?>
	<tr>
		<td><?php echo $data["id"]."/".date('Y', strtotime($data["date_creation"])); ?></td>
		<td><?php echo $data["date_creation"]; ?></td>
		<td><?php echo $data["description"]; ?></td>
		<td><?php echo $data["specialite"]; ?></td>
		<td><?php echo $data["nom_prenom"]; ?></td>
		<td><?php echo $data["cin"]; ?></td>
		<td>
			<form method="post" action="DossierEdit.php">
				<input type="text" readonly hidden value="<?php echo $data["id"]?>" name="dos_id">
				<input type="submit" value="Modifier">
			</form><br>
			<form method="post" action="AddOrdonnances.php">
				<input type="text" readonly hidden value="<?php echo $data["id"]?>" name="dos_id">
				<button type="submit"><i class="fa fa-plus"></i>&nbsp;Ordonnances</button>
			</form><br>
			<form method="post" action="AddAnalyses.php">
				<input type="text" readonly hidden value="<?php echo $data["id"]?>" name="dos_id">
				<button type="submit"><i class="fa fa-plus"></i>&nbsp;Analyses</button>
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