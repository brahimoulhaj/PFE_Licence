<?php
require('../sihaty/ConnectDB.php');
	
	$dossier_id = $_POST["dossier_id"];
	$file = basename($_FILES["file"]["name"]);
	
$target_dir = "../sihaty/ord/";
$target_file = $target_dir . $file;
$uploadOk = 1;
$fileType = strtolower(pathinfo($target_file,PATHINFO_EXTENSION));
// Check if image file is a actual image or fake image
if(isset($_POST["submit"])) {
    $check = getimagesize($_FILES["file"]["tmp_name"]);
    if($check !== false) {
        echo "File is an image - " . $check["mime"] . ".";
        $uploadOk = 1;
    } else {
        echo "File is not an image.";
        $uploadOk = 0;
    }
}
// Allow certain file formats
if($fileType != "pdf" && $fileType != "PDF" ) {
    echo "Sorry, only PDF files are allowed.";
    $uploadOk = 0;
}
// Check if $uploadOk is set to 0 by an error
if ($uploadOk == 0) {
    echo "Sorry, your file was not uploaded.";
// if everything is ok, try to upload file
} else {
    if (move_uploaded_file($_FILES["file"]["tmp_name"], $target_file)) {
			$nf = "0";
			$sql = $conn->query("SELECT COUNT(*) AS nf FROM ordonnances WHERE file='$file'");
			while($c = $sql->fetch(PDO::FETCH_ASSOC)){
				$nf = $c["nf"];
			}
			echo $c["nf"];
			if($nf <> "0"){$conn->exec("UPDATE ordonnances SET file='$file' WHERE dossier_id=$dossier_id");}
			else{$conn->exec("INSERT INTO ordonnances(file, dossier_id) VALUES('$file',$dossier_id)");}
			?>
			<script>window.location.href = "Dossier.php";</script>
			<?php
        echo "The file ". basename( $_FILES["file"]["name"]). " has been uploaded.";
    } else {
        echo "Sorry, there was an error uploading your file.";
    }
}

?>

<a href="Dossier.php"><button>Retour</button></a>