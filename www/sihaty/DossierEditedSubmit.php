<?php 
require('../sihaty/ConnectDB.php');
try{
$specialite = $_POST["specialite"];
$description = $_POST["description"];
$id = $_POST["dos_id"];

$conn->exec("UPDATE  `dossiers` SET description=\"$description\",specialite='$specialite' WHERE id=$id");
$response["success"] = true;
	
}catch(PDOException $e){
	echo "error::::   ".$e;
}

?>


<?php
if($response["success"] == true){
?>
<script>
	window.location.href = "Dossier.php";
</script>
<?php
}else{
	?>
<button onClick="window.history.back();">Retour</button>
<?php
}
?>