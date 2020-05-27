<?php
require('../sihaty/ConnectDB.php');
$result = $conn->query("SELECT * FROM touslesrdv");
while($cmp = $result->fetch(PDO::FETCH_ASSOC)){
		$response[] = $cmp;
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