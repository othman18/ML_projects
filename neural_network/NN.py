import numpy
# import matplotlib.pyplot as plt
import scipy.special
# import imageio
# from PIL import Image

# from skimage import color, io
# from skimage.transform import resize


class NeuralNetwork:
    def __init__(self, input_nodes, hidden_nodes, output_nodes, learning_rate):
        self.inodes = input_nodes
        self.hnodes = hidden_nodes
        self.onodes = output_nodes
        self.lr = learning_rate

        # link weight matrices, wih := input-hidden and who := hidden-output
        self.wih = numpy.random.normal(0.0, pow(self.hnodes, -0.5), (self.hnodes, self.inodes))
        self.who = numpy.random.normal(0.0, pow(self.onodes, -0.5), (self.onodes, self.hnodes))

        # sigmoid function
        self.activation_function = lambda x: scipy.special.expit(x)

    def train(self, input_list, target_list):
        # convert inputs to 2d array
        inputs = numpy.array(input_list, ndmin=2).T
        targets = numpy.array(target_list, ndmin=2).T

        # calculate signals into hidden layer
        hidden_inputs = numpy.dot(self.wih, inputs)

        # calculate signals coming out of hidden
        hidden_outputs = self.activation_function(hidden_inputs)

        # calculate signals coming into final output layer
        final_inputs = numpy.dot(self.who, hidden_outputs)

        # calculate signals coming out of final output layer
        final_outputs = self.activation_function(final_inputs)

        # calculate error = target - actual
        output_errors = targets - final_outputs

        # hidden layer error is the output_error, split by weights, recombined at hidden nodes
        hidden_errors = numpy.dot(self.who.T, output_errors)

        # update the weights for the link between the hidden and output layers
        self.who += self.lr * numpy.dot((output_errors * final_outputs * (1 - final_outputs)),
                                        numpy.transpose(hidden_outputs))

        # update the weights for the link between the input and hidden layers
        self.wih += self.lr * numpy.dot((hidden_errors * hidden_outputs * (1 - hidden_outputs)),
                                        numpy.transpose(inputs))

    def query(self, input_list):
        # convert inputs to 2d array
        inputs = numpy.array(input_list, ndmin=2).T

        # calculate signals coming into hidden
        hidden_inputs = numpy.dot(self.wih, inputs)

        # calculate signals coming out of hidden
        hidden_outputs = self.activation_function(hidden_inputs)

        # calculate signals coming into final output layer
        final_inputs = numpy.dot(self.who, hidden_outputs)

        # calculate signals coming out of final output layer
        final_outputs = self.activation_function(final_inputs)

        return final_outputs


"""
def import_image(img_dir='dataSets/test.png'):
    image = Image.open(img_dir)

    image = numpy.array(image)

    lina_color = io.imread(img_dir)  #
    lina_color = 255.0 * 0 - lina_color
    lina_gray = color.rgb2gray(lina_color)  # convert to 2d array
    image_resized = resize(lina_gray, (28, 28), anti_aliasing=True)
    img_data = image_resized.reshape(784)

    plt.imshow(image_resized, cmap='Greys', interpolation='None')
    plt.show()

    # then scale data to range from 0.01 to 1.0
    img_data = (img_data / 255.0 * 0.99) + 0.01

    return img_data
"""


def main(epoch=5):
    input_nodes = 784  # 28 * 28 is the input size
    hidden_nodes = 200
    output_nodes = 10
    lr = 0.1
    n = NeuralNetwork(input_nodes, hidden_nodes, output_nodes, lr)

    data_file = open("dataSets/mnist_train.csv", 'r')
    data_list = data_file.readlines()
    data_file.close()

    print("training")
    epoch = 1
    for e in range(epoch):
        print("epoch = %s / %s" % (e + 1, epoch))
        counter = 1
        for record in data_list:
            if counter % 1000 == 0:
                print("\t done = %s %%" % (round(100 * counter / len(data_list), 1)))
            all_values = record.split(',')
            # scale input
            inputs = (numpy.asfarray(all_values[1:]) / 255.0 * 0.99) + 0.01
            # init target expectation
            targets = numpy.zeros(output_nodes) + 0.01
            targets[int(all_values[0])] = 0.99
            n.train(inputs, targets)
            counter += 1

    test_data_file = open("dataSets/mnist_train.csv", 'r')
    test_data_list = test_data_file.readlines()
    test_data_file.close()

    print("Testing")
    score = []
    counter = 1

    for record in test_data_list:
        if counter % 100 == 0:
            print("\t done = %s %%" % (round(100 * counter / len(test_data_list), 1)))
        counter += 1
        all_values = record.split(',')
        correct_label = all_values[0]
#        print("correct_label = %s" % correct_label)
        # scale input
        inputs = (numpy.asfarray(all_values[1:]) / 255.0 * 0.99) + 0.01
        # init target expectation
        outputs = n.query(inputs)
        label = numpy.argmax(outputs)
        if label == int(correct_label):
            score.append(1)
        else:
            score.append(0)
#        print("NN guess = %s" % label)

    # calculate the performance score, the fraction of correct answers
    print("accuracy of NN = %s" % (sum(score) / len(score)))

    print("done")

    # this is still in progress to import your own image
    """
    img_data = import_image()
    outputs = n.query(img_data)
    print(outputs)
    label = numpy.argmax(outputs)
    print(label)
    """


if __name__ == '__main__':
    main(1)
